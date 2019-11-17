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
		// 去掉logo
		getActionBar().setDisplayShowHomeEnabled(false);
		// 设置actionbar标题
		Util.setActionBarTilte(getActionBar(), "消息");
		setContentView(R.layout.activity_main_frame);
		// 初始化
		initData();
		initView();
		initEvent();
		getUserInfo();
		// 如果不是管理员那么隐藏相关的Tabbar按钮和Fragment
		if (!isAdmin) {
			findViewById(R.id.ll_manage).setVisibility(View.GONE);
			list.remove(1);
			adapterViewPager.notifyDataSetChanged();
		}
		setStatusColor();
	}

	private void setStatusColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 系统版本大于19
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
		// 读取
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

	// 初始化回调
	private void initEvent() {
		findViewById(R.id.img_tabbar_home).setOnClickListener(this);
		findViewById(R.id.img_tabbar_manage).setOnClickListener(this);
		findViewById(R.id.img_tabbar_mine).setOnClickListener(this);
		// 监听ViewPager变化
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// 设置每个Tab显示的图片
				imgHome.setImageResource(R.drawable.foot_nav_home_normal);
				imgManage.setImageResource(R.drawable.foot_nav_manage_normal);
				imgMine.setImageResource(R.drawable.foot_nav_mine_normal);
				if (isAdmin)
					switch (arg0) {
					case 0:
						imgHome.setImageResource(R.drawable.foot_nav_home_current);
						Util.setActionBarTilte(getActionBar(), "主页");
						break;
					case 1:
						imgManage
								.setImageResource(R.drawable.foot_nav_manage_current);
						Util.setActionBarTilte(getActionBar(), "管理系统");
						break;
					case 2:
						imgMine.setImageResource(R.drawable.foot_nav_mine_current);
						Util.setActionBarTilte(getActionBar(), "我");
						break;
					default:
						break;
					}
				else {
					switch (arg0) {
					case 0:
						imgHome.setImageResource(R.drawable.foot_nav_home_current);
						Util.setActionBarTilte(getActionBar(), "主页");
						break;
					case 1:
						imgMine.setImageResource(R.drawable.foot_nav_mine_current);
						Util.setActionBarTilte(getActionBar(), "我");
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
		// 为ViewPager绑定适配器
		adapterViewPager = new ViewPagerAdapter(list,
				getSupportFragmentManager());
		viewPager.setAdapter(adapterViewPager);
		// 同时加载三个页面
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
		// 监听Tab 点击Tab跳转到对应界面
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
