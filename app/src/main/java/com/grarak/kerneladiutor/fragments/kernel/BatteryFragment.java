/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
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
package com.grarak.kerneladiutor.fragments.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.fragments.DescriptionFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.kernel.battery.Battery;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.SelectView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.StatsView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class BatteryFragment extends RecyclerViewFragment {

    private StatsView mLevel;
    private StatsView mVoltage;
    private StatsView mDC;

    private static int sBatteryLevel;
    private static int sBatteryVoltage;

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        levelInit(items);
        voltageInit(items);
        mDC = new StatsView();
        if (Battery.hasDc()) {
        items.add(mDC);
	    }
        if (Battery.hasForceFastCharge()) {
            forceFastChargeInit(items);
        }
        if (Battery.hasBlx()) {
            blxInit(items);
        }
        chargeRateInit(items);
        fastchgCurrentInit(items);
    }

    @Override
    protected void postInit() {
        super.postInit();

        if (itemsSize() > 2) {
            addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
        }
        if (Battery.hasCapacity(getActivity())) {
            addViewPagerFragment(DescriptionFragment.newInstance(getString(R.string.capacity),
                    Battery.getCapacity(getActivity()) + getString(R.string.mah)));
        }
    }

    private void levelInit(List<RecyclerViewItem> items) {
        mLevel = new StatsView();
        mLevel.setTitle(getString(R.string.level));

        items.add(mLevel);
    }

    private void voltageInit(List<RecyclerViewItem> items) {
        mVoltage = new StatsView();
        mVoltage.setTitle(getString(R.string.voltage));

        items.add(mVoltage);
    }

    private void forceFastChargeInit(List<RecyclerViewItem> items) {
        SwitchView forceFastCharge = new SwitchView();
        forceFastCharge.setTitle(getString(R.string.usb_fast_charge));
        forceFastCharge.setSummary(getString(R.string.usb_fast_charge_summary));
        forceFastCharge.setChecked(Battery.isForceFastChargeEnabled());
        forceFastCharge.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                Battery.enableForceFastCharge(isChecked, getActivity());
            }
        });

        items.add(forceFastCharge);
    }

    private void blxInit(List<RecyclerViewItem> items) {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 0; i <= 100; i++) {
            list.add(String.valueOf(i));
        }

        SeekBarView blx = new SeekBarView();
        blx.setTitle(getString(R.string.blx));
        blx.setSummary(getString(R.string.blx_summary));
        blx.setItems(list);
        blx.setProgress(Battery.getBlx());
        blx.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Battery.setBlx(position, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(blx);
    }
    private void fastchgCurrentInit(List<RecyclerViewItem> items) {
        CardView fastchargeCard = new CardView(getActivity());
        fastchargeCard.setTitle(getString(R.string.fc_switch));

        if (Battery.hasFCEnable()) {
            SwitchView fcswitch = new SwitchView();
            fcswitch.setSummary(getString(R.string.fc_switch_summary));
            fcswitch.setChecked(Battery.isFCEnabled());
            fcswitch.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    Battery.enableFC(isChecked, getActivity());
                }
            });

            fastchargeCard.addItem(fcswitch);
        }
        
      if (Battery.hasfastchgCurrent()) {
            SeekBarView fastchgCurrent = new SeekBarView();
            fastchgCurrent.setTitle(getString(R.string.fastchg_current));
            fastchgCurrent.setSummary(getString(R.string.fastchg_current_summary));
            fastchgCurrent.setUnit(getString(R.string.ma));
            fastchgCurrent.setMax(3000);
            fastchgCurrent.setMin(100);
            fastchgCurrent.setOffset(10);
            fastchgCurrent.setProgress(Battery.getfastchgCurrent() / 10 - 10);
            fastchgCurrent.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    Battery.setfastchgCurrent((position + 10) * 10, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

             fastchargeCard.addItem(fastchgCurrent);
        }
       if (fastchargeCard.size() > 0) {
            items.add(fastchargeCard);
        }
        }

    private void chargeRateInit(List<RecyclerViewItem> items) {
        CardView chargeRateCard = new CardView(getActivity());
        chargeRateCard.setTitle(getString(R.string.charge_rate));

        if (Battery.hasChargeRateEnable()) {
            SwitchView chargeRate = new SwitchView();
            chargeRate.setSummary(getString(R.string.charge_rate));
            chargeRate.setChecked(Battery.isChargeRateEnabled());
            chargeRate.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    Battery.enableChargeRate(isChecked, getActivity());
                }
            });

            chargeRateCard.addItem(chargeRate);
        }
        
        if (Battery.hasChargeProfile()) {
			List<String> freqs = new ArrayList<>();
            SelectView chargeProfile = new SelectView();
            chargeProfile.setTitle(getString(R.string.charge_profile));
            chargeProfile.setSummary(getString(R.string.charge_profile_summary));
            chargeProfile.setItems(Battery.getProfilesMenu(getActivity()));
            chargeProfile.setItem(Battery.getProfiles());
            chargeProfile.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    Battery.setchargeProfile(position, getActivity());
                }
            });

            chargeRateCard.addItem(chargeProfile);
        }

        if (Battery.hasChargingCurrent()) {
			if (Battery.isThunder()){
            SeekBarView chargingCurrent = new SeekBarView();
            chargingCurrent.setTitle(getString(R.string.charging_current));
            chargingCurrent.setSummary(getString(R.string.charging_current_summary));
            chargingCurrent.setUnit(getString(R.string.ma));
            chargingCurrent.setMax(1500);
            chargingCurrent.setMin(100);
            chargingCurrent.setOffset(10);
            chargingCurrent.setProgress(Battery.getChargingCurrent() / 10 - 10);
            chargingCurrent.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    Battery.setChargingCurrent((position + 10) * 10, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            chargeRateCard.addItem(chargingCurrent);
        }}
        
         if (Battery.hasUSBChargingCurrent()) {
            SeekBarView usbchargingCurrent = new SeekBarView();
            usbchargingCurrent.setTitle(getString(R.string.usb_charging_current));
            usbchargingCurrent.setSummary(getString(R.string.usb_charging_current_summary));
            usbchargingCurrent.setUnit(getString(R.string.ma));
            usbchargingCurrent.setMax(1000);
            usbchargingCurrent.setMin(100);
            usbchargingCurrent.setOffset(10);
            usbchargingCurrent.setProgress(Battery.getUSBChargingCurrent() / 10 - 10);
            usbchargingCurrent.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    Battery.setUSBChargingCurrent((position + 10) * 10, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            chargeRateCard.addItem(usbchargingCurrent);
        }

        if (chargeRateCard.size() > 0) {
            items.add(chargeRateCard);
        }
    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            sBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        }
    };

    @Override
    protected void refresh() {
        super.refresh();
        if (mLevel != null) {
            mLevel.setStat(sBatteryLevel + "%");
        }
        if (mVoltage != null) {
            mVoltage.setStat(sBatteryVoltage + getString(R.string.mv));
        }
        
        if (mDC != null) {
			if (Battery.getDc() >= 10000) {
			float dc = Battery.getDc() /1000;
			if (Battery.isCharge()){
			mDC.setTitle("DisCharge Rate");
            mDC.setStat(String.valueOf(dc) + (" mA"));}
            else{
			mDC.setTitle("Charge Rate");
            mDC.setStat(String.valueOf(dc) + (" mA"));}}
			else {
			float cd = Battery.getDc();
			if (Battery.isCharge()){
			mDC.setTitle("DisCharge Rate");
            mDC.setStat(String.valueOf(cd) + (" mA"));}
            else{
			mDC.setTitle("Charge Rate");
            mDC.setStat(String.valueOf(cd) + (" mA"));}}

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mBatteryReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }

}
