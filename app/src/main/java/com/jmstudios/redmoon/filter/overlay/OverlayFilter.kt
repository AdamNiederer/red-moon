/*
 * Copyright (c) 2016  Marien Raat <marienraat@riseup.net>
 * Copyright (c) 2017  Stephen Michel <s@smichel.me>
 * SPDX-License-Identifier: GPL-3.0+
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 *     Copyright (c) 2015 Chris Nguyen
 *     Copyright (c) 2016 Zoraver <https://github.com/Zoraver>
 *
 *     Permission to use, copy, modify, and/or distribute this software
 *     for any purpose with or without fee is hereby granted, provided
 *     that the above copyright notice and this permission notice appear
 *     in all copies.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 *     WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 *     WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE
 *     AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 *     CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 *     OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *     NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 *     CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
 package com.jmstudios.redmoon.filter.overlay

import android.content.Context
import com.jmstudios.redmoon.filter.Filter
import com.jmstudios.redmoon.model.Profile
import com.jmstudios.redmoon.util.*

class OverlayFilter (private val mContext: Context) : Filter {
    private val mOverlay = Overlay(mContext)
    private val mOrientationReceiver = OrientationChangeReceiver(mOverlay)
    private val mBrightnessManager = BrightnessManager(mContext)
    private val mCurrentAppMonitor = CurrentAppMonitor(mContext)

    override fun start() {
        Log.i("start()")
        mOverlay.show()
        mOrientationReceiver.register(mContext)
        mBrightnessManager.lower()
        mCurrentAppMonitor.start()
        EventBus.register(mOverlay)
        EventBus.register(mBrightnessManager)
        EventBus.register(mCurrentAppMonitor)
    }

    override fun setColor(profile: Profile) {
        Log.i("setColor($profile)")
        mOverlay.color = profile.filterColor
    }

    override fun stop() {
        Log.i("stop()")
        mOverlay.hide()
        mOrientationReceiver.unregister(mContext)
        mCurrentAppMonitor.stop()
        EventBus.unregister(mOverlay)
        EventBus.unregister(mBrightnessManager)
        EventBus.unregister(mCurrentAppMonitor)
        mBrightnessManager.restore()
    }

    companion object : Logger()
}
