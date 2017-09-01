/*
 * Copyright (C) 2015-2017 Willi Ye <williye97@gmail.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.grarak.kerneladiutor.utils.kernel.network;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;
import com.grarak.kerneladiutor.utils.root.RootUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by dani020110 on 31.08.16.
 */

public class Network {
    private static final String TCP_AVAILABLE_CONGESTIONS = "/proc/sys/net/ipv4/tcp_available_congestion_control";
    private static final String HOSTNAME_KEY = "net.hostname";

    public static void setHostname(String value, Context context) {
        run(Control.setProp(HOSTNAME_KEY, value), HOSTNAME_KEY, context);
    }

    public static String getHostname() {
        return RootUtils.getProp(HOSTNAME_KEY);
    }

    public static void setTcpCongestion(String tcpCongestion, Context context) {
        run("sysctl -w net.ipv4.tcp_congestion_control=" + tcpCongestion, TCP_AVAILABLE_CONGESTIONS, context);
    }

    public static String getTcpCongestion() {
        return getTcpAvailableCongestions().get(0);
    }

    public static List<String> getTcpAvailableCongestions() {
        return new ArrayList<>(Arrays.asList(Utils.readFile(TCP_AVAILABLE_CONGESTIONS).split(" ")));
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.NETWORK, id, context);
    }

}