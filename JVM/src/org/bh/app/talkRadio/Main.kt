package org.bh.app.talkRadio

import LatteFX.*
import LatteFX.PaneOrGroup.*
import org.bh.app.talkRadio.ui.*

/**
 * @author Ben Leggiero
 * @since 2018-04-30
 */

class Main : LatteFXMain(
        appInfo = MyTalkRadioInfo,
        appConfig = MyTalkRadioConfig,
        onStart = { _, primaryWindow ->
    primaryWindow?.show()
})



object MyTalkRadioInfo: LatteAppInfo {
    override val appName = "My Talk Radio"
}



private object MyTalkRadioConfig: LatteAppConfig {
    override val automaticallySetUpSystemMenuBar = true
    override val automaticallyGeneratePrimaryWindow = true
    override val primaryParent = group.main
    override val startingWrapperGenerator = { MyTalkRadioContentWrapper() }
}
