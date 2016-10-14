package com.haibin.qiaqia.personal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.login.LoginPassWordActivity;
import com.haibin.qiaqia.utils.SPUtils;
import com.haibin.qiaqia.widget.UpDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MineFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.school)
    TextView school;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.lin_info)
    LinearLayout linInfo;
    @BindView(R.id.title_bar1)
    TextView titleBar1;
    @BindView(R.id.img_head1)
    ImageView imgHead1;
    @BindView(R.id.account1)
    RelativeLayout account1;
    @BindView(R.id.title_bar)
    TextView titleBar;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.account)
    RelativeLayout account;
    /*@BindView(R.id.ll_icon)
    LinearLayout llIcon;*/
    @BindView(R.id.tv_wait_pay)
    TextView tvWaitPay;
    @BindView(R.id.tv_wait_get)
    TextView tvWaitGet;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_after_salas)
    TextView tvAfterSalas;
    @BindView(R.id.ll_wallte_realname)
    LinearLayout llWallteRealname;
    @BindView(R.id.or_img)
    ImageView orImg;
    @BindView(R.id.credit)
    RelativeLayout credit;
    @BindView(R.id.point_img9)
    ImageView pointImg9;
    @BindView(R.id.ll_guanli)
    RelativeLayout llGuanli;
    @BindView(R.id.or_img1)
    ImageView orImg1;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.rl_evaluation)
    RelativeLayout rlEvaluation;
    @BindView(R.id.point_img)
    ImageView pointImg;
    @BindView(R.id.ll_collect)
    RelativeLayout llCollect;
    @BindView(R.id.point_img1)
    ImageView pointImg1;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.rl_point)
    RelativeLayout rlPoint;
    @BindView(R.id.or_img5)
    ImageView orImg5;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.or_img8)
    ImageView orImg8;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.rl_updata)
    RelativeLayout rlUpdata;
    @BindView(R.id.or_img3)
    ImageView orImg3;
    @BindView(R.id.mine_message)
    ImageView mineMessage;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.img_03)
    ImageView img03;
    @BindView(R.id.about)
    RelativeLayout about;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    private View view;
    private String strPhone;
    private String strImg;
    private String strName;
    private int loginId;
    private int version;
    private String apkurl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);

        version = (int) SPUtils.getParam(getActivity(), Constants.LOGIN_INFO, Constants.LOGIN_VERSION, 0);
        apkurl = (String) SPUtils.getParam(getActivity(), Constants.LOGIN_INFO, Constants.LOGIN_APK_URL, "");
        ininView();
        return view;
    }

    private void ininView() {
        credit.setOnClickListener(this);
        llGuanli.setOnClickListener(this);
        llCollect.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlUpdata.setOnClickListener(this);
        about.setOnClickListener(this);
        tvWaitPay.setOnClickListener(this);
        tvWaitGet.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        tvAfterSalas.setOnClickListener(this);
        account.setOnClickListener(this);
        mineMessage.setOnClickListener(this);
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        strPhone = (String) SPUtils.getParam(getActivity(), Constants.USER_LOGIN, Constants.LOGIN_PHONE, "");
        strImg = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO,Constants.INFO_IMG,"");
        strName = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO,Constants.INFO_NAME,"");
        loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
        if (loginId != 0){
            name.setText(strName);
            tvPhone.setText(strPhone);
            Glide.with(getActivity()).load(strImg).centerCrop().into(imgHead);
        }else{
            tvPhone.setVisibility(View.GONE);
            tvBalance.setVisibility(View.GONE);
        }
    }

    private void initData() {

    }


    @Override
    public void onClick(View v) {
        if (loginId != 0){
            switch (v.getId()){
                case R.id.credit:
                    startActivity(new Intent(getActivity(),AddressActivity.class));
                    break;
                case R.id.ll_guanli:
                    startActivity(new Intent(getActivity(),CouponActivity.class));
                    break;
                case R.id.ll_collect:
                    startActivity(new Intent(getActivity(),CollectionActivity.class));
                    break;
                case R.id.rl_feedback:
                    startActivity(new Intent(getActivity(),SuggestActivity.class));
                    break;
                case R.id.rl_updata:
                    if (version > getVersion()) {//大于当前版本升级
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("检测到新版本，是否更新？")
                                .setConfirmText("确定")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        UpDialog upDataDialog = new UpDialog(getActivity(),apkurl);
                                        upDataDialog.setCanceledOnTouchOutside(false);
                                        upDataDialog.setCanceledOnTouchOutside(false);
                                        upDataDialog.show();

                                    }
                                }).show();
                    } else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("已经是最新版本了")
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                }).show();
                    }
                    break;
                case R.id.about:
                    startActivity(new Intent(getActivity(),AboutOursActivity.class));
                    break;
                case R.id.tv_wait_pay:
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),MyOrderActivity.class);
                    intent.putExtra("currentPage",0);
                    startActivity(intent);
                    break;
                case R.id.tv_wait_get:
                    Intent intent1 = new Intent();
                    intent1.setClass(getActivity(),MyOrderActivity.class);
                    intent1.putExtra("currentPage",1);
                    startActivity(intent1);
                    break;
                case R.id.tv_finish:
                    Intent intent2 = new Intent();
                    intent2.setClass(getActivity(),MyOrderActivity.class);
                    intent2.putExtra("currentPage",2);
                    startActivity(intent2);
                    break;
                case R.id.tv_after_salas:
                    Intent intent3 = new Intent();
                    intent3.setClass(getActivity(),MyOrderActivity.class);
                    intent3.putExtra("currentPage",3);
                    startActivity(intent3);
                    break;
                case R.id.mine_message:
                    startActivity(new Intent(getActivity(),PushActivity.class));
                    break;
            }
        }else {
            switch (v.getId()){
                case R.id.credit:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.ll_guanli:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.ll_collect:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.rl_feedback:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.rl_updata:
                    startActivity(new Intent(getActivity(),PushActivity.class));
                    break;
                case R.id.about:
                    startActivity(new Intent(getActivity(),AboutOursActivity.class));
                    break;
                case R.id.tv_wait_pay:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.tv_wait_get:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.tv_finish:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.tv_after_salas:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
                case R.id.account:
                    startActivity(new Intent(getActivity(),LoginPassWordActivity.class));
                    break;
            }
        }
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
