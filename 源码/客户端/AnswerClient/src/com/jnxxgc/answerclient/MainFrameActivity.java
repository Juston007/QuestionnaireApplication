package com.jnxxgc.answerclient;

import java.text.ParseException;
import java.util.ArrayList;

import com.jnxxgc.answerclient.adapter.ViewPagerAdapter;
import com.jnxxgc.answerclient.model.UserModel;
import com.jnxxgc.answerclient.util.SharedPreferencesUtil;
import com.jnxxgc.answerclient.util.SystemBarTintManager;
import com.jnxxgc.answerclient.util.Util;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainFrameActivity extends FragmentActivity implements
		OnClickListener {

	public static UserModel loginUserModel = null;
	private boolean isAdmin = false;

	private ViewPager viewPager;
	private ImageView imgHome, imgManage, imgMine;

	private ArrayList<Fragment> list = new ArrayList<Fragment>();

	private ViewPagerAdapter adapterViewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// ����actionbar����
		Util.setActionBarTilte(getActionBar(), "��Ϣ");
		setContentView(R.layout.activity_main_frame);
		// ��ʼ��
		initData();
		initView();
		initEvent();
		getUserInfo();
		// ������ǹ���Ա��ô������ص�Tabbar��ť��Fragment
		if (!isAdmin) {
			findViewById(R.id.ll_manage).setVisibility(View.GONE);
			list.remove(1);
			adapterViewPager.notifyDataSetChanged();
		}
		setStatusColor();
	}

	private void setStatusColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // ϵͳ�汾����19
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.statusColor);
	}

	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void getUserInfo() {
		// ��ȡ
		String uid = SharedPreferencesUtil.shared.getUid();
		String name = SharedPreferencesUtil.shared.getName();
		String className = SharedPreferencesUtil.shared.getClassName();
		isAdmin = SharedPreferencesUtil.shared.isAdmin();
		try {
			MainFrameActivity.loginUserModel = new UserModel(uid, name,
					className, "1970-01-1T0:0:0", "1970-01-1T0:0:0", false,
					false, isAdmin);
		} catch (ParseException e) {
			Log.d("Juston", e.getMessage());
		}
	}

	// ��ʼ���ص�
	private void initEvent() {
		findViewById(R.id.img_tabbar_home).setOnClickListener(this);
		findViewById(R.id.img_tabbar_manage).setOnClickListener(this);
		findViewById(R.id.img_tabbar_mine).setOnClickListener(this);
		// ����ViewPager�仯
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// ����ÿ��Tab��ʾ��ͼƬ
				imgHome.setImageResource(R.drawable.foot_nav_home_normal);
				imgManage.setImageResource(R.drawable.foot_nav_manage_normal);
				imgMine.setImageResource(R.drawable.foot_nav_mine_normal);
				if (isAdmin)
					switch (arg0) {
					case 0:
						imgHome.setImageResource(R.drawable.foot_nav_home_current);
						Util.setActionBarTilte(getActionBar(), "��ҳ");
						break;
					case 1:
						imgManage
								.setImageResource(R.drawable.foot_nav_manage_current);
						Util.setActionBarTilte(getActionBar(), "����ϵͳ");
						break;
					case 2:
						imgMine.setImageResource(R.drawable.foot_nav_mine_current);
						Util.setActionBarTilte(getActionBar(), "��");
						break;
					default:
						break;
					}
				else {
					switch (arg0) {
					case 0:
						imgHome.setImageResource(R.drawable.foot_nav_home_current);
						Util.setActionBarTilte(getActionBar(), "��ҳ");
						break;
					case 1:
						imgMine.setImageResource(R.drawable.foot_nav_mine_current);
						Util.setActionBarTilte(getActionBar(), "��");
						break;
					default:
						break;
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.vp_main_Pager);
		imgHome = (ImageView) findViewById(R.id.img_tabbar_home);
		imgManage = (ImageView) findViewById(R.id.img_tabbar_manage);
		imgMine = (ImageView) findViewById(R.id.img_tabbar_mine);
		// ΪViewPager��������
		adapterViewPager = new ViewPagerAdapter(list,
				getSupportFragmentManager());
		viewPager.setAdapter(adapterViewPager);
		// ͬʱ��������ҳ��
		viewPager.setOffscreenPageLimit(2);
	}

	private void initData() {
		HomeFragment homeFragment = new HomeFragment();
		ManageFragment manageFragment = new ManageFragment();
		MineFragment mineFragment = new MineFragment();
		list.add(homeFragment);
		list.add(manageFragment);
		list.add(mineFragment);
	}

	@Override
	public void onClick(View arg0) {
		// ����Tab ���Tab��ת����Ӧ����
		switch (arg0.getId()) {
		case R.id.img_tabbar_home:
			viewPager.setCurrentItem(0);
			break;
		case R.id.img_tabbar_manage:
			viewPager.setCurrentItem(1);
			break;
		case R.id.img_tabbar_mine:
			viewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}

}
