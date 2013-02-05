package com.capstone.hearingtest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class PlayFrequency {
	// The code for this class is based off of
	// http://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
	
	private static int duration; // seconds
	private static int sampleRate;
	private static int numSamples;
	private static double sample[];
	private static double freqOfTone; // hz
	private static byte[] generatedSnd;

	/**
	 * Generates the necessary byte array in preparation to be played. Must call
	 * {@link playSound()} to output the tone.
	 * 
	 * @param dur
	 *            Duration of sound.
	 * @param freq
	 *            Frequency of sound
	 */
	public static void genTone(int dur, int freq) {

		setVars(dur, freq);

		// fill out the array
		for (int i = 0; i < numSamples; ++i) {
			sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
		}
		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalized.
		int i = 0;
		int idx = 0;
		int ramp = numSamples / 40; // Amplitude ramp as a percent of sample
									// count

		for (i = 0; i < ramp; ++i) { // Ramp amplitude up (to avoid clicks)
			double dVal = sample[i];
			// Ramp up to maximum
			final short val = (short) ((dVal * 32767 * i / ramp));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		for (i = i; i < numSamples - ramp; ++i) { // Max amplitude for most of
													// the samples
			double dVal = sample[i];
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		for (i = i; i < numSamples; ++i) { // Ramp amplitude down
			double dVal = sample[i];
			// Ramp down to zero
			final short val = (short) ((dVal * 32767 * (numSamples - i) / ramp));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		// -----------------------without ramping------
		// int idx = 0;
		// for (final double dVal : sample) {
		// // scale to maximum amplitude
		// final short val = (short) ((dVal * 32767));
		// // in 16 bit wav PCM, first byte is the low order byte
		// generatedSnd[idx++] = (byte) (val & 0x00ff);
		// generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		// }
	}

	/**
	 * @Pre Must call {@link genTone(dur, freq)} first.
	 * @Post Out puts the sound generated in {@link genTone(dur, freq)}.
	 */
	public static void playSound() {
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
				AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		audioTrack.play();
		Log.d("Main", "played");
	}

	/**
	 * Sets all necessary fields. Should only be called from {@link genTone(dur,
	 * freq)}
	 * 
	 * @param d
	 *            duration
	 * @param f
	 *            frequency
	 */
	private static void setVars(int d, int f) {
		duration = d; // seconds
		sampleRate = 8000;
		numSamples = duration * sampleRate;
		sample = new double[numSamples];
		freqOfTone = 1440; // hz
		generatedSnd = new byte[2 * numSamples];
	}
}
