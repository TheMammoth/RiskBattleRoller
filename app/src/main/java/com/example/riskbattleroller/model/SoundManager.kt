package com.example.riskbattleroller.model

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.riskbattleroller.R

class SoundManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private var diceRollSoundId: Int = -1
    private var soundLoaded = false

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        diceRollSoundId = soundPool?.load(context, R.raw.dice_roll, 1) ?: -1

        soundPool?.setOnLoadCompleteListener { _, _, status ->
            soundLoaded = (status == 0)
        }
    }

    fun playDiceRoll() {
        if (soundLoaded && soundPool != null) {
            soundPool?.play(diceRollSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
