package com.orcanote.orcaswitchlibrary.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.orcanote.orcaswitch.ui.widget.OrcaSwitch;
import com.orcanote.orcaswitchlibrary.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OrcaSwitch.OnCheckedChangeListener {

    @BindView(R.id.btn_switch)
    OrcaSwitch mSwitch;
    @BindView(R.id.btn_enable)
    TextView   btnEnable;
    @BindView(R.id.btn_check)
    TextView   btnCheck;
    @BindView(R.id.btn_check_without_event)
    TextView   btnCheckWithoutEvent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSwitch.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.btn_enable)
    public void onEnableClick() {
        if (mSwitch.isDisabled()) {
            mSwitch.setDisabled(false);
            btnEnable.setText(R.string.bm_disable);
            ToastUtils.showMessage(this, R.string.bm_enabled);
        } else {
            mSwitch.setDisabled(true);
            btnEnable.setText(R.string.bm_enable);
            ToastUtils.showMessage(this, R.string.bm_disabled);
        }
    }

    @OnClick(R.id.btn_check)
    public void onCheckClick() {
        if (mSwitch.isChecked()) {
            mSwitch.setChecked(false, true);
            btnCheck.setText(R.string.bm_check);
            btnCheckWithoutEvent.setText(R.string.bm_check_without_event);
        } else {
            mSwitch.setChecked(true, true);
            btnCheck.setText(R.string.bm_uncheck);
            btnCheckWithoutEvent.setText(R.string.bm_uncheck_without_event);
        }
    }

    @OnClick(R.id.btn_check_without_event)
    public void onCheckWithoutEventClick() {
        if (mSwitch.isChecked()) {
            mSwitch.setChecked(false, false);
            btnCheck.setText(R.string.bm_check);
            btnCheckWithoutEvent.setText(R.string.bm_check_without_event);
            ToastUtils.showMessage(this, R.string.bm_unchecked_without_event);
        } else {
            mSwitch.setChecked(true, false);
            btnCheck.setText(R.string.bm_uncheck);
            btnCheckWithoutEvent.setText(R.string.bm_uncheck_without_event);
            ToastUtils.showMessage(this, R.string.bm_checked_without_event);
        }
    }

    @Override
    public void onCheckedChanged(OrcaSwitch orcaSwitch, boolean checked) {
        if (checked) {
            btnCheck.setText(R.string.bm_uncheck);
            btnCheckWithoutEvent.setText(R.string.bm_uncheck_without_event);
            ToastUtils.showMessage(this, R.string.bm_checked);
        } else {
            btnCheck.setText(R.string.bm_check);
            btnCheckWithoutEvent.setText(R.string.bm_check_without_event);
            ToastUtils.showMessage(this, R.string.bm_unchecked);
        }
    }
}
