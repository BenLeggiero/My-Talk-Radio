package org.bh.app.talkRadio

import LatteFX.*
import javafx.scene.*
import org.bh.app.talkRadio.ui.*
import java.beans.*

/**
 * @author Ben Leggiero
 * @since 2018-04-30
 */

class Main : LatteFXMain(
        appInfo = MyTalkRadioInfo,
        appConfig = MyTalkRadioConfig,
        onStart = { _, primaryWindow ->
    primaryWindow?.contentWrapper = MyTalkRadioContentWrapper()
    primaryWindow?.show()
})



public object MyTalkRadioInfo: LatteAppInfo {
    override val appName = "My Talk Radio"
}



private object MyTalkRadioConfig: LatteAppConfig {
    override val automaticallySetUpSystemMenuBar = true
    override val primaryGroup = LatteGroup.main
    override val startingWrapperGenerator = { MyTalkRadioContentWrapper() }
}
