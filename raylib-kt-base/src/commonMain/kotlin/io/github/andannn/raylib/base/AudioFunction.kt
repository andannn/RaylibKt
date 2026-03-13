package io.github.andannn.raylib.base

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.UByteVar

interface AudioFunction : MusicStreamFunction, AudioDeviceFunction, WaveSoundFunction

fun AudioFunction(): AudioFunction = object :
    AudioFunction,
    WaveSoundFunction by DefaultWaveSoundFunction(),
    MusicStreamFunction by DefaultMusicStreamFunction(),
    AudioDeviceFunction by DefaultAudioDeviceFunction() {}

interface AudioDeviceFunction {
    fun initAudioDevice()
    fun closeAudioDevice()
    fun isAudioDeviceReady(): Boolean
    fun setMasterVolume(volume: Float)
    fun getMasterVolume(): Float
}

private class DefaultAudioDeviceFunction : AudioDeviceFunction {
    override fun initAudioDevice() {
        raylib.interop.InitAudioDevice()
    }

    override fun closeAudioDevice() {
        raylib.interop.CloseAudioDevice()
    }

    override fun isAudioDeviceReady(): Boolean {
        return raylib.interop.IsAudioDeviceReady()
    }

    override fun setMasterVolume(volume: Float) {
        raylib.interop.SetMasterVolume(volume)
    }

    override fun getMasterVolume(): Float {
        return raylib.interop.GetMasterVolume()
    }
}

interface WaveSoundFunction {
    fun loadWave(fileName: String): CValue<Wave>
    fun loadWaveFromMemory(fileType: String, fileData: CPointer<UByteVar>?, dataSize: Int): CValue<Wave>
    fun isWaveValid(wave: CValue<Wave>): Boolean

    fun loadSoundFromWave(wave: CValue<Wave>): CValue<Sound>
    fun loadSoundAlias(source: CValue<Sound>): CValue<Sound>
    fun isSoundValid(sound: CValue<Sound>): Boolean

    fun updateSound(sound: CValue<Sound>, data: COpaquePointer?, sampleCount: Int)

    fun unloadWave(wave: CValue<Wave>)
    fun playSound(sound: CValue<Sound>)
    fun stopSound(sound: CValue<Sound>)
    fun pauseSound(sound: CValue<Sound>)
    fun resumeSound(sound: CValue<Sound>)
    fun isSoundPlaying(sound: CValue<Sound>): Boolean
    fun setSoundVolume(sound: CValue<Sound>, volume: Float)
    fun setSoundPitch(sound: CValue<Sound>, pitch: Float)
    fun setSoundPan(sound: CValue<Sound>, pan: Float)

    fun waveCopy(wave: CValue<Wave>): CValue<Wave>
    fun waveCrop(wave: CPointer<Wave>?, initFrame: Int, finalFrame: Int)
    fun waveFormat(wave: CPointer<Wave>?, sampleRate: Int, sampleSize: Int, channels: Int)
    fun loadWaveSamples(wave: CValue<Wave>): CPointer<FloatVar>?
    fun unloadWaveSamples(samples: CPointer<FloatVar>?)
}

private class DefaultWaveSoundFunction : WaveSoundFunction {
    override fun loadWave(fileName: String): CValue<Wave> {
        return raylib.interop.LoadWave(fileName)
    }

    override fun loadWaveFromMemory(fileType: String, fileData: CPointer<UByteVar>?, dataSize: Int): CValue<Wave> {
        return raylib.interop.LoadWaveFromMemory(fileType, fileData, dataSize)
    }

    override fun isWaveValid(wave: CValue<Wave>): Boolean {
        return raylib.interop.IsWaveValid(wave)
    }

    override fun loadSoundFromWave(wave: CValue<Wave>): CValue<Sound> {
        return raylib.interop.LoadSoundFromWave(wave)
    }

    override fun loadSoundAlias(source: CValue<Sound>): CValue<Sound> {
        return raylib.interop.LoadSoundAlias(source)
    }

    override fun isSoundValid(sound: CValue<Sound>): Boolean {
        return raylib.interop.IsSoundValid(sound)
    }

    override fun updateSound(sound: CValue<Sound>, data: COpaquePointer?, sampleCount: Int) {
        raylib.interop.UpdateSound(sound, data, sampleCount)
    }

    override fun unloadWave(wave: CValue<Wave>) {
        raylib.interop.UnloadWave(wave)
    }

    override fun playSound(sound: CValue<Sound>) {
        raylib.interop.PlaySound(sound)
    }

    override fun stopSound(sound: CValue<Sound>) {
        raylib.interop.StopSound(sound)
    }

    override fun pauseSound(sound: CValue<Sound>) {
        raylib.interop.PauseSound(sound)
    }

    override fun resumeSound(sound: CValue<Sound>) {
        raylib.interop.ResumeSound(sound)
    }

    override fun isSoundPlaying(sound: CValue<Sound>): Boolean {
        return raylib.interop.IsSoundPlaying(sound)
    }

    override fun setSoundVolume(sound: CValue<Sound>, volume: Float) {
        raylib.interop.SetSoundVolume(sound, volume)
    }

    override fun setSoundPitch(sound: CValue<Sound>, pitch: Float) {
        raylib.interop.SetSoundPitch(sound, pitch)
    }

    override fun setSoundPan(sound: CValue<Sound>, pan: Float) {
        raylib.interop.SetSoundPan(sound, pan)
    }

    override fun waveCopy(wave: CValue<Wave>): CValue<Wave> {
        return raylib.interop.WaveCopy(wave)
    }

    override fun waveCrop(wave: CPointer<Wave>?, initFrame: Int, finalFrame: Int) {
        raylib.interop.WaveCrop(wave, initFrame, finalFrame)
    }

    override fun waveFormat(wave: CPointer<Wave>?, sampleRate: Int, sampleSize: Int, channels: Int) {
        raylib.interop.WaveFormat(wave, sampleRate, sampleSize, channels)
    }

    override fun loadWaveSamples(wave: CValue<Wave>): CPointer<FloatVar>? {
        return raylib.interop.LoadWaveSamples(wave)
    }

    override fun unloadWaveSamples(samples: CPointer<FloatVar>?) {
        raylib.interop.UnloadWaveSamples(samples)
    }
}

interface MusicStreamFunction {
    fun isMusicValid(music: CValue<Music>): Boolean
    fun playMusicStream(music: CValue<Music>)
    fun isMusicStreamPlaying(music: CValue<Music>): Boolean
    fun updateMusicStream(music: CValue<Music>)
    fun stopMusicStream(music: CValue<Music>)
    fun pauseMusicStream(music: CValue<Music>)
    fun resumeMusicStream(music: CValue<Music>)
    fun seekMusicStream(music: CValue<Music>, position: Float)
    fun setMusicVolume(music: CValue<Music>, volume: Float)
    fun setMusicPitch(music: CValue<Music>, pitch: Float)
    fun setMusicPan(music: CValue<Music>, pan: Float)
    fun getMusicTimeLength(music: CValue<Music>): Float
    fun getMusicTimePlayed(music: CValue<Music>): Float
}

private class DefaultMusicStreamFunction : MusicStreamFunction {
    override fun isMusicValid(music: CValue<Music>): Boolean {
        return raylib.interop.IsMusicValid(music)
    }
    override fun playMusicStream(music: CValue<Music>) {
        raylib.interop.PlayMusicStream(music)
    }

    override fun isMusicStreamPlaying(music: CValue<Music>): Boolean {
        return raylib.interop.IsMusicStreamPlaying(music)
    }

    override fun updateMusicStream(music: CValue<Music>) {
        raylib.interop.UpdateMusicStream(music)
    }

    override fun stopMusicStream(music: CValue<Music>) {
        raylib.interop.StopMusicStream(music)
    }

    override fun pauseMusicStream(music: CValue<Music>) {
        raylib.interop.PauseMusicStream(music)
    }

    override fun resumeMusicStream(music: CValue<Music>) {
        raylib.interop.ResumeMusicStream(music)
    }

    override fun seekMusicStream(music: CValue<Music>, position: Float) {
        raylib.interop.SeekMusicStream(music, position)
    }

    override fun setMusicVolume(music: CValue<Music>, volume: Float) {
        raylib.interop.SetMusicVolume(music, volume)
    }

    override fun setMusicPitch(music: CValue<Music>, pitch: Float) {
        raylib.interop.SetMusicPitch(music, pitch)
    }

    override fun setMusicPan(music: CValue<Music>, pan: Float) {
        raylib.interop.SetMusicPan(music, pan)
    }

    override fun getMusicTimeLength(music: CValue<Music>): Float {
        return raylib.interop.GetMusicTimeLength(music)
    }

    override fun getMusicTimePlayed(music: CValue<Music>): Float {
        return raylib.interop.GetMusicTimePlayed(music)
    }
}