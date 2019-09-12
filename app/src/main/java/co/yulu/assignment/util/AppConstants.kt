package co.yulu.assignment.util

import co.yulu.assignment.BuildConfig

object AppConstants {

    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +
            "&referrer=utm_source%3DsettingsTab%26utm_campaign%3Drateus"

    const val DATABASE_NAME = "Demo.db"

    const val ONE_SECOND = 1000

    const val ONE_MINUTE = ONE_SECOND * 60

    const val ONE_HOUR = ONE_MINUTE * 60

    const val ONE_DAY = ONE_HOUR * 24

}

object SharedPreferenceConstants {
    const val SP_FILE_KEY = "Demo`Preferences"
}

object IntentKeys {
    const val messageApiResponse = "messageApiResponse"
    const val story = "story"
    const val unlockAt = "unlockAt"
    const val alreadyAUser = "alreadyAUser"
    const val fragmentType = "fragmentType"
    const val episode = "episode"
    const val nextEpisodeName = "nextEpisodeName"
    const val storyName = "storyName"
    const val isBlocker = "isBlocker"
    const val blockerUnlocked = "blockerUnlocked"
    const val horrorId = "horrorID"
    const val lovestoryId = "lovestoryID"
}