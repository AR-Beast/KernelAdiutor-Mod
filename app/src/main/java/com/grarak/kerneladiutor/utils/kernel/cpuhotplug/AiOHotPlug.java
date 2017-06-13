/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
 * Copyright (C) 2017 Ayush Rathore <ayushrathore12501@gmail.com>
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
package com.grarak.kerneladiutor.utils.kernel.cpuhotplug;

import android.content.Context;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 07.05.16.
 * Updated for AiO Hotplug by Ayush Rathore 13.06.2017
 */
public class AiOHotPlug {

    private static final String HOTPLUG_AiO_HOTPLUG = "/sys/kernel/AiO_HotPlug";
    private static final String HOTPLUG_AiO_HOTPLUG_ENABLE = HOTPLUG_AiO_HOTPLUG + "/toggle";
    private static final String HOTPLUG_AiO_HOTPLUG_ONLINE_CORES = HOTPLUG_AiO_HOTPLUG + "/cores";
    private static final String HOTPLUG_AiO_HOTPLUG_BIG_CORES = HOTPLUG_AiO_HOTPLUG + "/big_cores";
    private static final String HOTPLUG_AiO_HOTPLUG_LITTLE_CORES = HOTPLUG_AiO_HOTPLUG + "/LITTLE_cores";

    public static void setAiOHotPlugOnlineCores(int value, Context context) {
        run(Control.write(String.valueOf(value), HOTPLUG_AiO_HOTPLUG_ONLINE_CORES),
                HOTPLUG_AiO_HOTPLUG_ONLINE_CORES, context);
    }

    public static int getAiOHotPlugOnlineCores() {
        return Utils.strToInt(Utils.readFile(HOTPLUG_AiO_HOTPLUG_ONLINE_CORES));
    }

    public static boolean hasAiOHotPlugOnlineCores() {
        return Utils.existFile(HOTPLUG_AiO_HOTPLUG_ONLINE_CORES);
    }
    
        public static void setAiOHotPlugBigCores(int value, Context context) {
        run(Control.write(String.valueOf(value), HOTPLUG_AiO_HOTPLUG_BIG_CORES),
                HOTPLUG_AiO_HOTPLUG_BIG_CORES, context);
    }

    public static int getAiOHotPlugBigCores() {
        return Utils.strToInt(Utils.readFile(HOTPLUG_AiO_HOTPLUG_BIG_CORES));
    }

    public static boolean hasAiOHotPlugBigCores() {
        return Utils.existFile(HOTPLUG_AiO_HOTPLUG_BIG_CORES);
    }
    
   public static void setAiOHotPlugLittleCores(int value, Context context) {
        run(Control.write(String.valueOf(value), HOTPLUG_AiO_HOTPLUG_LITTLE_CORES),
                HOTPLUG_AiO_HOTPLUG_LITTLE_CORES, context);
    }

    public static int getAiOHotPlugLittleCores() {
        return Utils.strToInt(Utils.readFile(HOTPLUG_AiO_HOTPLUG_LITTLE_CORES));
    }

    public static boolean hasAiOHotPlugLittleCores() {
        return Utils.existFile(HOTPLUG_AiO_HOTPLUG_LITTLE_CORES);
    }
    
    public static void enableAiOHotPlug(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", HOTPLUG_AiO_HOTPLUG_ENABLE),
                HOTPLUG_AiO_HOTPLUG_ENABLE, context);
    }

    public static boolean isAiOHotPlugEnabled() {
        return Utils.readFile(HOTPLUG_AiO_HOTPLUG_ENABLE).equals("1");
    }

    public static boolean hasAiOHotPlugEnable() {
        return Utils.existFile(HOTPLUG_AiO_HOTPLUG_ENABLE);
    }

    public static boolean supported() {
        return (Utils.existFile(HOTPLUG_AiO_HOTPLUG));
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.CPU_HOTPLUG, id, context);
    }

}

