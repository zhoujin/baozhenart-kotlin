package com.markjin.artmall.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markjin.artmall.AppContext;
import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.Address;
import com.markjin.artmall.db.bean.AddressArea;
import com.markjin.artmall.db.bean.AddressCity;
import com.markjin.artmall.db.bean.AddressProvince;
import com.markjin.artmall.db.bean.Region;
import com.markjin.artmall.db.dao.AddressDao;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.RegionDao;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ToastUtil;
import com.markjin.artmall.view.wheelview.ArrayWheelAdapter;
import com.markjin.artmall.view.wheelview.OnWheelChangedListener;
import com.markjin.artmall.view.wheelview.WheelView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 地址添加与更新 if editor ->bundle:(address)
 */

public class AddressAddActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_address_name;
    private EditText et_address_phone;
    private EditText et_address_detail;
    private TextView address_district;

    private String is_default;

    private List<String> province_name = new ArrayList<>();
    private Map<String, String[]> cityMaps = new HashMap<>();
    private Map<String, String[]> areaMaps = new HashMap<>();
    //
    private List<AddressProvince> provinceList = new ArrayList<>();
    private Map<String, Integer[]> mCitisIds = new HashMap<>();
    private Map<String, Integer[]> mDistrictIds = new HashMap<>();

    private List<AddressArea> provinces = new ArrayList<AddressArea>();
    private List<AddressArea> citys = new ArrayList<AddressArea>();
    private List<AddressArea> areas = new ArrayList<AddressArea>();

    private Address address;
    private int select_province_position, select_city_position, select_area_position;
    private int current_province_id, current_city_id, current_area_id;
    private String current_province_name, current_city_name, current_area_name;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        initRegionData();
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.rl_address_district).setOnClickListener(this);
        findViewById(R.id.bt_sure).setOnClickListener(this);
        TextView top_title = findViewById(R.id.tv_title);

        et_address_name = findViewById(R.id.et_address_name);
        et_address_phone = findViewById(R.id.et_address_phone);
        et_address_detail = findViewById(R.id.et_address_detail);
        address_district = findViewById(R.id.address_district);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("editor")) {
            top_title.setText(R.string.address_update);
            address = (Address) getIntent().getExtras().getSerializable("editor");
            et_address_name.setText(address.address_name);
            et_address_name.setSelection(address.address_name.length());
            et_address_phone.setText(address.address_phone);
            et_address_detail.setText(address.detail_address);
            address_district.setText(address.province + address.country + address.district);
            current_province_id = Integer.valueOf(address.province_id);
            current_city_id = Integer.valueOf(address.country_id);
            current_area_id = Integer.valueOf(address.district_id);
            current_province_name = address.province;
            current_city_name = address.country;
            current_area_name = address.district;
            is_default = address.is_default;
        } else {
            top_title.setText(R.string.address_add);
        }
    }

    private void initRegionData() {
        RegionDao regionDao = BaseDaoFactory.getInstance().getDataHelper(RegionDao.class, Region.class);
        Region region = new Region();
        List<Region> result = regionDao.query(region);
        for (int i = 0; i < result.size(); i++) {
            AddressArea addressArea = new AddressArea();
            addressArea.region_id = result.get(i).region_id;
            addressArea.parent_id = result.get(i).parent_id;
            addressArea.region_name = result.get(i).region_name;
            addressArea.region_type = result.get(i).region_type;
            if (result.get(i).region_type.equals("1")) {
                province_name.add(result.get(i).region_name);
                provinces.add(addressArea);

            } else if (result.get(i).region_type.equals("2")) {
                citys.add(addressArea);
            } else if (result.get(i).region_type.equals("3")) {
                areas.add(addressArea);
            }
        }
        initData();
    }

    private void initData() {
        for (int a = 0; a < provinces.size(); a++) {
            if (address != null) {
                if (address.province.equals(provinces.get(a).region_name)) {
                    current_province_id = Integer.valueOf(provinces.get(a).region_id);
                }
            }
            AddressProvince province = new AddressProvince();
            province.parent_id = provinces.get(a).parent_id;
            province.region_name = provinces.get(a).region_name;
            province.region_id = provinces.get(a).region_id;
            province.region_type = provinces.get(a).region_type;

            List<AddressCity> addressCities = new ArrayList<>();
            for (int b = 0; b < citys.size(); b++) {
                if (citys.get(b).parent_id.equals(province.region_id)) {
                    if (address != null) {
                        if (address.country.equals(citys.get(b).region_name)) {
                            current_city_id = Integer.valueOf(citys.get(b).region_id);
                        }
                    }
                    AddressCity city = new AddressCity();
                    city.parent_id = citys.get(b).parent_id;
                    city.region_id = citys.get(b).region_id;
                    city.region_name = citys.get(b).region_name;
                    city.region_type = citys.get(b).region_type;
                    addressCities.add(city);
                    List<AddressArea> addressAreas = new ArrayList<>();
                    for (int c = 0; c < areas.size(); c++) {
                        if (address != null) {
                            if (address.district.equals(areas.get(c).region_name)) {
                                current_area_id = Integer.valueOf(areas.get(c).region_id);
                            }
                        }
                        if (areas.get(c).parent_id.equals(citys.get(b).region_id)) {
                            AddressArea area = new AddressArea();
                            area.parent_id = areas.get(c).parent_id;
                            area.region_id = areas.get(c).region_id;
                            area.region_name = areas.get(c).region_name;
                            area.region_type = areas.get(c).region_type;
                            addressAreas.add(area);
                        }
                    }
                    city.areas = addressAreas;
                }
                province.citys = addressCities;
            }
            provinceList.add(province);
        }

        for (int i = 0; i < provinceList.size(); i++) {
            List<AddressCity> cityList = provinceList.get(i).citys;
            String[] cityNames = new String[cityList.size()];
            Integer[] cityIds = new Integer[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                cityNames[j] = cityList.get(j).region_name;
                cityIds[j] = Integer.valueOf(cityList.get(j).region_id);
                List<AddressArea> areaList = cityList.get(j).areas;
                String[] areaName = new String[areaList.size()];
                Integer[] areaIds = new Integer[areaList.size()];
                for (int k = 0; k < areaList.size(); k++) {
                    areaName[k] = areaList.get(k).region_name;
                    areaIds[k] = Integer.valueOf(areaList.get(k).region_id);
                }
                areaMaps.put(cityNames[j], areaName);
                mDistrictIds.put(cityNames[j], areaIds);
            }
            mCitisIds.put(provinceList.get(i).region_name, cityIds);
            cityMaps.put(provinceList.get(i).region_name, cityNames);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.rl_address_district:
                showAddressDialog();
                break;
            case R.id.bt_sure:
                saveAddress();
                break;
        }
    }

    private void showAddressDialog() {
        final Dialog dialog = new Dialog(this, R.style.ActivityTransparent);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_address_area, null);
        final WheelView mViewProvince = view.findViewById(R.id.lv_province_picker);
        final WheelView mViewCity = view.findViewById(R.id.lv_city_picker);
        final WheelView mViewDistrict = view.findViewById(R.id.lv_area_picker);
        //显示的条数
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);

        if (select_province_position == -1 && select_city_position == -1 && select_area_position == -1) {
            mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(AddressAddActivity.this, province_name.toArray(new String[0])));
            current_province_id = Integer.valueOf(provinces.get(0).region_id);
            updateCities(mViewProvince, mViewCity, mViewDistrict);
            updateAreas(mViewCity, mViewDistrict);
        } else {
            mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(AddressAddActivity.this, province_name.toArray(new String[0])));
            current_province_id = Integer.valueOf(provinces.get(0).region_id);
            mViewProvince.setCurrentItem(select_province_position);
            updateCities(mViewProvince, mViewCity, mViewDistrict);
            updateAreas(mViewCity, mViewDistrict);
        }
        mViewProvince.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                select_province_position = newValue;
                select_area_position = -1;
                updateCities(mViewProvince, mViewCity, mViewDistrict);
            }
        });
        mViewCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                select_city_position = newValue;
                updateAreas(mViewCity, mViewDistrict);
            }
        });
        mViewDistrict.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                select_area_position = newValue;
                if (select_area_position == -1) {
                    if (mDistrictIds.get(current_city_name).length > 0) {
                        current_area_id = mDistrictIds.get(current_city_name)[newValue];
                        current_area_name = areaMaps.get(current_city_name)[newValue];
                    } else {
                        current_area_id = -1;
                        current_area_name = "";
                    }
                } else {
                    if (!TextUtils.isEmpty(current_city_name)) {
                        if (mDistrictIds.get(current_city_name).length > 0) {
                            current_area_id = mDistrictIds.get(current_city_name)[select_area_position];
                            current_area_name = areaMaps.get(current_city_name)[select_area_position];
                        } else {
                            current_area_id = -1;
                            current_area_name = "";
                        }
                    }
                }
            }
        });
        view.findViewById(R.id.bt_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.bt_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                address_district.setText(current_province_name + current_city_name + current_area_name);
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setWindowAnimations(R.style.commonAnimDialogStyle);
        dialog.getWindow().setLayout(AppContext.Companion.getInstance().getDisplayMetrics().widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void updateCities(WheelView mViewProvince, WheelView mViewCity, WheelView mViewDistrict) {
        int pCurrent = mViewProvince.getCurrentItem();
        if (select_province_position == -1) {
            select_province_position = pCurrent;
            current_province_id = Integer.valueOf(provinces.get(pCurrent).region_id);
            current_province_name = province_name.toArray(new String[0])[pCurrent];
            String[] cities = cityMaps.get(current_province_name);
            if (cities == null) {
                cities = new String[]{""};
            }
            mViewCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));
            mViewCity.setCurrentItem(0);
            current_city_id = mCitisIds.get(current_province_name)[0];
        } else {
            current_province_id = Integer.parseInt(provinces.get(select_province_position).region_id);
            current_province_name = province_name.toArray(new String[0])[select_province_position];
            String[] cities = cityMaps.get(current_province_name);
            if (cities == null) {
                cities = new String[]{""};
            }
            if (cities.length == 0) {
                cities = new String[1];
                cities[0] = "";
            }
            mViewCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));
            mViewCity.setCurrentItem(0);
        }
        updateAreas(mViewCity, mViewDistrict);
    }

    private void updateAreas(WheelView mViewCity, WheelView mViewDistrict) {
        int pCurrent = mViewCity.getCurrentItem();
        if (select_city_position == -1) {
            if (cityMaps.get(current_province_name).length > 0) {
                current_city_name = cityMaps.get(current_province_name)[pCurrent];
                current_city_id = mCitisIds.get(current_province_name)[pCurrent];
                String[] areas = areaMaps.get(current_city_name);

                if (areas == null) {
                    areas = new String[]{""};
                }
                mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(this, areas));
                if (select_area_position == -1) {
                    if (areas.length > 0) {
                        mViewDistrict.setCurrentItem(0);
                        current_area_name = areas[0];
                        if (!TextUtils.isEmpty(current_area_name)) {
                            current_area_id = mDistrictIds.get(current_city_name)[0];
                        } else {
                            current_area_id = -1;
                        }
                    } else {
                        current_area_id = -1;
                        current_area_name = "";
                    }
                } else {
                    if (areas.length > 0) {
                        mViewDistrict.setCurrentItem(select_area_position);
                        if (select_area_position > areas.length) {
                            current_area_name = areas[0];
                            mViewDistrict.setCurrentItem(0);
                        } else {
                            current_area_name = areas[select_area_position];
                            mViewDistrict.setCurrentItem(select_area_position);
                        }
                        if (!TextUtils.isEmpty(current_area_name)) {
                            current_area_id = mDistrictIds.get(current_city_name)[select_area_position];
                        } else {
                            current_area_id = -1;
                        }
                    } else {
                        current_area_id = -1;
                        current_area_name = "";
                    }
                }
            } else {
                String[] areas = null;
                if (areas == null) {
                    areas = new String[]{""};
                }
                current_area_id = -1;
                current_city_id = -1;
                current_city_name = "";
                current_area_name = "";
                mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(this, areas));
            }
        } else {
            mViewCity.setCurrentItem(select_city_position);
            if (cityMaps.containsKey(current_province_name) && cityMaps.get(current_province_name).length > 0) {
                current_city_name = cityMaps.get(current_province_name)[select_city_position];
                current_city_id = mCitisIds.get(current_province_name)[select_city_position];
                String[] areas = areaMaps.get(current_city_name);

                if (areas == null) {
                    areas = new String[]{""};
                }
                if (areas.length == 0) {
                    areas = new String[1];
                    areas[0] = "";
                }
                mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
                if (select_area_position == -1) {
                    if (areas.length > 0) {
                        mViewDistrict.setCurrentItem(0);
                        current_area_name = areas[0];
                        if (!TextUtils.isEmpty(current_area_name)) {
                            current_area_id = mDistrictIds.get(current_city_name)[0];
                        } else {
                            current_area_id = -1;
                        }
                    }
                } else {
                    if (areas.length > 0) {
                        mViewDistrict.setCurrentItem(select_area_position);
                        if (select_area_position > areas.length) {
                            current_area_name = areas[0];
                            mViewDistrict.setCurrentItem(0);
                        } else {
                            if (select_area_position < areas.length) {
                                current_area_name = areas[select_area_position];
                                mViewDistrict.setCurrentItem(select_area_position);
                            }
                        }
                        if (!TextUtils.isEmpty(current_area_name)) {
                            if (select_area_position < mDistrictIds.get(current_city_name).length) {
                                current_area_id = mDistrictIds.get(current_city_name)[select_area_position];
                            }
                        } else {
                            current_area_id = -1;
                        }
                    }
                }
            } else {
                String[] areas = null;
                if (areas == null) {
                    areas = new String[]{""};
                }
                current_area_id = -1;
                current_city_id = -1;
                current_city_name = "";
                current_area_name = "";
                mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
            }
        }
    }

    private void saveAddress() {
        if (TextUtils.isEmpty(et_address_name.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.address_name_empty_hint);
            return;
        }
        if (TextUtils.isEmpty(et_address_phone.getText().toString().trim()) || et_address_phone.getText().toString().trim().length() != 11) {
            ToastUtil.showMessage(this, R.string.phone_error_hint);
            return;
        }
        if (TextUtils.isEmpty(address_district.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.address_district_empty_hint);
            return;
        }
        if (TextUtils.isEmpty(et_address_detail.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.address_detail_empty_hint);
            return;
        }
        Address update = new Address();
        update.user_id = PreferenceUtil.getUserInfo().user_id;
        update.address_name = et_address_name.getText().toString().trim();
        update.address_phone = et_address_phone.getText().toString().trim();
        update.detail_address = et_address_detail.getText().toString().trim();

        update.province = current_province_name;
        update.country = current_city_name;
        update.district = current_area_name;

        update.province_id = String.valueOf(current_province_id);
        update.country_id = String.valueOf(current_city_id);
        update.district_id = String.valueOf(current_area_id);
        if (!TextUtils.isEmpty(is_default)) {
            address.is_default = is_default;
        }
        AddressDao addressDao = BaseDaoFactory.getInstance().getDataHelper(AddressDao.class, Address.class);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("editor")) {
            update.update_time = String.valueOf(System.currentTimeMillis());
            Address where = new Address();
            where.user_id = PreferenceUtil.getUserInfo().user_id;
            where.address_id = address.address_id;
            addressDao.update(update, where);
            ToastUtil.showMessage(this, R.string.success_update);
            finishActivity(this);
        } else {
            update.add_time = String.valueOf(System.currentTimeMillis());
            update.update_time = String.valueOf(System.currentTimeMillis());
            addressDao.insert(update);
            ToastUtil.showMessage(this, R.string.success_add);
            finishActivity(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
