package org.bh.app.talkRadio

import LatteFX.*
import org.bh.app.talkRadio.ui.*

/**
 * @author Ben Leggiero
 * @since 2018-04-30
 */

class Main : LatteFXMain({ commandLineArguments, primaryWindow ->
    primaryWindow?.contentWrapper = MyTalkRadioContentWrapper()
    primaryWindow?.show()
})
