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
	
	public static final int LEFT_EAR_ONLY = 1;
	public static final int RIGHT_EAR_ONLY = 2;
	public static final int BOTH_EARS = 3;
	public AudioTrack audioTrack = null; 
	
	public  PlayFrequency(int duration){
		setVars(duration, 0);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
				sampleRate, 
				AudioFormat.CHANNEL_OUT_STEREO, 
				AudioFormat.ENCODING_PCM_16BIT, 
				generatedSnd.length,
				AudioTrack.MODE_STREAM);
	}
	
	/**
	 * Generates the necessary byte array in preparation to be played. Must call
	 * {@link playSound()} to output the tone.
	 * 
	 * @param dur
	 *            Duration of sound.
	 * @param freq
	 *            Frequency of sound
	 */
	public void genTone(int dur, int freq) {
//		setVars(dur, freq);
		freqOfTone = freq;
		// fill out the array
		for (int i = 0; i < numSamples; ++i) {
//			sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
			sample[i] = Math.sin(freqOfTone * (2 * Math.PI) * i / sampleRate);
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

		// -----without ramping------
		// int idx = 0;
		// for (final double dVal : sample) {
		// // scale to maximum amplitude
		// final short val = (short) ((dVal * 32767));
		// // in 16 bit wav PCM, first byte is the low order byte
		// generatedSnd[idx++] = (byte) (val & 0x00ff);
		// generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		// }
//			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
//				sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
//				AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
//				AudioTrack.MODE_STATIC);
			
			
	}

	/**
	 * @pre Must call {@link genTone(dur, freq)} first.
	 * @post Outputs the sound generated in {@link genTone(dur, freq)} to both
	 *       the left and right channels.
	 */
	public  void playSound() {
		playSound(1f, 1f);
	}

	/**
	 * @pre Must call {@link genTone(dur, freq)} first.
	 * @post Outputs the sound generated in {@link genTone(dur, freq)} to the
	 *       channels specified by the params.
	 * @param left Float. Sets volume level for left ear. 0 = no output. 
	 * @param right Float. Sets volume level for right ear. 0 = no output. 
	 */
	public void playSound(float left, float right) {
//		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
//				sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
//				AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
//				AudioTrack.MODE_STATIC);
		Float leftVolume = left;
		Float rightVolume = right;
//		if (left)
//			leftVolume = AudioTrack.getMaxVolume();
//		else
//			leftVolume = AudioTrack.getMinVolume();
//
//		if (right)
//			rightVolume = AudioTrack.getMaxVolume();
//		else
//			rightVolume = AudioTrack.getMinVolume();
		
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		audioTrack.setStereoVolume(leftVolume, rightVolume);
//		audioTrack.setLoopPoints(0, 66150, -1);
		audioTrack.play();
		Log.d("Main", "played");
	}
	/**
	 * 
	 *  @pre Must call {@link genTone(dur, freq)} first.
	 * @post Outputs the sound generated in {@link genTone(dur, freq)} to the
	 *       channels specified by the params.
	 * @param left Float. Sets volume level for left ear. 0 = no output. 
	 * @param right Float. Sets volume level for right ear. 0 = no output. 
	 * @param whichEar INT. LEFT_EAR_ONLY. RIGHT_EAR_ONLY. BOTH_EARS.
	 */
	public  void playSound(float left, float right, int whichEar) {
		switch(whichEar){
		case LEFT_EAR_ONLY: 
			playSound(left, 0);
			break;
		case RIGHT_EAR_ONLY: 
			playSound(0, right);
			break;
		case BOTH_EARS: 
			playSound(left, right);
			break;

		}
	}
	
	public void start(){
		audioTrack.play();
	}
	
	public void stop(){
		audioTrack.stop();
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
		sampleRate = 44100;
		numSamples = duration * sampleRate;
		sample = new double[numSamples];
		freqOfTone = f; // hz
		generatedSnd = new byte[2 * numSamples];
	}
}
