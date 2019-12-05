package com.moyck.recoder251.domain

import java.io.File

/**
 * @package:com.moyck.recoder251.domain
 * @date on 2019/12/5 15:16
 * @author  Moyuk
 */
class RecoderItem(var file: File) {

    var isPlaying = false
    var progress = 0f
    var duration = 0

}