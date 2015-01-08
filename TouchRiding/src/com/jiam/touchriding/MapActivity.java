package com.jiam.touchriding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.jiam.touchriding.map.NMapCalloutCustomOldOverlay;
import com.jiam.touchriding.map.NMapPOIflagType;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapActivity extends NMapActivity {
	
	private static final String LOG_TAG = "MapActivity";
	private static final boolean DEBUG = false;	
	
	// set your API key which is registered for NMapViewer library.
	private static final String API_KEY = "e019278de710a8a5c7f520f28d5919a8";
	
	
	private MapContainerView mMapContainerView; //내위치 실시간 반영 인듯
	
	private NMapView mMapView;
	
	//지도 상태변경 컨트롤러 
	private NMapController mMapController;
	
	
	
	
	
	//기타변수
	//private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(126.978371, 37.5666091);
	
	//지도 줌 레벨
	//private static final int NMAP_ZOOMLEVEL_DEFAULT = 11;//작을수록 범위넒어짐
	//private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
	//private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
	//private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;
	
	
	private static final String KEY_ZOOM_LEVEL = "MapActivity.zoomLevel";
	private static final String KEY_CENTER_LONGITUDE = "MapActivity.centerLongitudeE6";
	private static final String KEY_CENTER_LATITUDE = "MapActivity.centerLatitudeE6";
	private static final String KEY_VIEW_MODE = "MapActivity.viewMode";
	private static final String KEY_TRAFFIC_MODE = "MapActivity.trafficMode";
	private static final String KEY_BICYCLE_MODE = "MapActivity.bicycleMode";
	
	
	private SharedPreferences mPreferences;												//임시자료저장
	
	private NMapOverlayManager mOverlayManager;											//지도위의 표시들 관리

	private NMapMyLocationOverlay mMyLocationOverlay;									//현재위치
	private NMapLocationManager mMapLocationManager;									//현재위치 메니져
	private NMapCompassManager mMapCompassManager;										//나침반

	
	private com.jiam.touchriding.map.NMapViewerResourceProvider mMapViewerResourceProvider;	//객체드로잉에 필요함

	private NMapPOIdataOverlay mFloatingPOIdataOverlay;									//오버레이아이템
	private NMapPOIitem mFloatingPOIitem;												//POI 아이템 클래스
	
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_map);
		//NMapView mMapView;
		
		
		// create map view
		mMapView = new NMapView(this);


		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(API_KEY);
		
		// set the activity content to the parent view
		setContentView(mMapView);
		
		mMapView.setClickable(true);
		
		// initialize map view
		mMapView.setClickable(true);
		mMapView.setEnabled(true);
		mMapView.setFocusable(true);
		mMapView.setFocusableInTouchMode(true);
		mMapView.requestFocus();
		
		
		
		
		// register listener for map state changes
		//mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);		//
		//mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);		//
		//mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);					//
		
		
		
		// use map controller to zoom in/out, pan and set map center, zoom level etc.
		mMapController = mMapView.getMapController();
		
		//자전거모드 기본설정
		mMapController.setMapViewBicycleMode(true);
		
		
		// use built in zoom controls
		//NMapView.LayoutParams lp = new NMapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
		//mMapView.setBuiltInZoomControls(true, lp);
		
				
		
		
		// create resource provider
		mMapViewerResourceProvider = new com.jiam.touchriding.map.NMapViewerResourceProvider(this);
		
		// set data provider listener
		super.setMapDataProviderListener(onDataProviderListener);//울집과 조금 다름

		// create overlay manager
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		// register callout overlay listener to customize it.
		mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
		// register callout overlay view listener to customize it.
		mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

		// location manager
		mMapLocationManager = new NMapLocationManager(this);
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

		// compass manager
		mMapCompassManager = new NMapCompassManager(this);

		// create my location overlay
		mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
		
		
		//아이템 표시
		showPOIdataOverlay();
		
		
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//		public void run() {
//		//TODO
//		startMyLocation();
//		finish();
//		}
//		}, 3000);



		
		
		
						
		//내 위치 표시
		//startMyLocation();
		
		//내위치 표시 중지
		//stopMyLocation();
		
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {

		stopMyLocation();

		super.onStop();
	}

	@Override
	protected void onDestroy() {

		// save map view state such as map center position and zoom level.
		saveInstanceState();

		super.onDestroy();
	}
	
	
	
	
//	// 내 위치 관련>???
//	private static final long AUTO_ROTATE_INTERVAL = 2000;
//	private final Handler mHnadler = new Handler();
//	private final Runnable mTestAutoRotation = new Runnable() {
//		@Override
//		public void run() {
////        	if (mMapView.isAutoRotateEnabled()) {
////    			float degree = (float)Math.random()*360;
////    			
////    			degree = mMapView.getRoateAngle() + 30;
////
////    			mMapView.setRotateAngle(degree);	
////            	
////            	mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);        		
////        	}
//		}
//	};
	
	
	
	
	
	//내 위치 표시 관련
	private void startMyLocation() {

		if (mMyLocationOverlay != null) {
			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
				mOverlayManager.addOverlay(mMyLocationOverlay);
			}

			if (mMapLocationManager.isMyLocationEnabled()) {

				if (!mMapView.isAutoRotateEnabled()) {
					mMyLocationOverlay.setCompassHeadingVisible(true);

					mMapCompassManager.enableCompass();

					mMapView.setAutoRotateEnabled(true, false);

					mMapContainerView.requestLayout();
				} else {
					stopMyLocation();
				}

				mMapView.postInvalidate();
			} else {
				boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
				if (!isMyLocationEnabled) {
					Toast.makeText(MapActivity.this, "Please enable a My Location source in system settings",
						Toast.LENGTH_LONG).show();

					Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(goToSettings);

					return;
				}
			}
		}
	}

	
	//현재위치 표시 중지
	private void stopMyLocation() {
		if (mMyLocationOverlay != null) {
			mMapLocationManager.disableMyLocation();

			if (mMapView.isAutoRotateEnabled()) {
				mMyLocationOverlay.setCompassHeadingVisible(false);

				mMapCompassManager.disableCompass();

				mMapView.setAutoRotateEnabled(false, false);

				mMapContainerView.requestLayout();
			}
		}
	}	
	
	
	
	
	//POI관련 맵상에 data표시
	private void showPOIdataOverlay() {

		// Markers for POI item
		int markerId = NMapPOIflagType.PIN;

		// set POI data
		
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
		
		//////////////////지암TEST////////////////////////////////////////////////////////
//		NMapPOIitem testItem1 = poiData.addPOIitem(126.997186, 37.609876, "7호관", markerId, 0);
//		testItem1.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
//		
//		NMapPOIitem testItem2 = poiData.addPOIitem(126.996727, 37.612326, "북악관", markerId, 0);
//		testItem2.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
//		
//		NMapPOIitem testItem3 = poiData.addPOIitem(126.993895, 37.612732, "성곡도서관", markerId, 0);
//		testItem3.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
//		
//		NMapPOIitem testItem4 = poiData.addPOIitem(126.999445, 37.611705, "과학관", markerId, 0);
//		testItem4.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
//		
//		NMapPOIitem testItem5 = poiData.addPOIitem(126.996149, 37.610876, "대운동장", markerId, 0);
//		testItem5.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
//		
//		NMapPOIitem testItem6 = poiData.addPOIitem(126.993952, 37.611758, "공학관", markerId, 0);
//		testItem6.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		////////////////////////////////////////////////////////////////////////////////////////////
		
		

		NMapPOIitem item01 = poiData.addPOIitem(126.605341, 37.555850, "아라서해갑문", markerId, 0);
		item01.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item02 = poiData.addPOIitem(126.800032, 37.598723, "아라한강갑문", markerId, 0);
		item02.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item03 = poiData.addPOIitem(126.913701, 37.534737, "여의도", markerId, 0);
		item03.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item04 = poiData.addPOIitem(127.066795, 37.530659, "뚝섬전망 콤플렉스", markerId, 0);
		item04.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item05 = poiData.addPOIitem(127.120201, 37.546319, "광나루 자전거공원", markerId, 0);
		item05.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item06 = poiData.addPOIitem(127.294821, 37.522183, "능내역", markerId, 0);
		item06.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item07 = poiData.addPOIitem(127.484226, 37.497024, "양평군립 미술관", markerId, 0);
		item07.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item08 = poiData.addPOIitem(127.541867, 37.407557, "이포보", markerId, 0);
		item08.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item09 = poiData.addPOIitem(127.605256, 37.326392, "여주보", markerId, 0);
		item09.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item10 = poiData.addPOIitem(127.680346, 37.277271, "강천보", markerId, 0);
		item10.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item11 = poiData.addPOIitem(127.809341, 37.111593, "비내섬", markerId, 0);
		item11.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item12 = poiData.addPOIitem(127.990297, 37.004483, "충주댐", markerId, 0);
		item12.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item13 = poiData.addPOIitem(127.312759, 37.553318, "밝은광장", markerId, 0);
		item13.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item14 = poiData.addPOIitem(127.370301, 37.662507, "새터삼거리", markerId, 0);
		item14.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item15 = poiData.addPOIitem(127.521618, 37.824675, "경강교", markerId, 0);
		item15.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item16 = poiData.addPOIitem(127.712907, 37.920865, "신매대교", markerId, 0);
		item16.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item17 = poiData.addPOIitem(127.903323, 36.989760, "충주 탄금대", markerId, 0);
		item17.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item18 = poiData.addPOIitem(127.989516, 36.848603, "수안보온천", markerId, 0);
		item18.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item19 = poiData.addPOIitem(128.031728, 36.751689, "새재길", markerId, 0);
		item19.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item20 = poiData.addPOIitem(128.141105, 36.655506, "문경 불정역", markerId, 0);
		item20.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item21 = poiData.addPOIitem(128.265696, 36.499010, "상주 상풍교", markerId, 0);
		item21.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item22 = poiData.addPOIitem(128.758728, 36.577711, "안동댐", markerId, 0);
		item22.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item23 = poiData.addPOIitem(128.251450, 36.431599, "상주보", markerId, 0);
		item23.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item24 = poiData.addPOIitem(128.306224, 36.359581, "낙단보", markerId, 0);
		item24.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item25 = poiData.addPOIitem(128.349636, 36.236628, "구미보", markerId, 0);
		item25.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item26 = poiData.addPOIitem(128.400740, 36.015241, "칠곡보", markerId, 0);
		item26.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item27 = poiData.addPOIitem(128.465247, 35.841210, "강정고령보", markerId, 0);
		item27.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item28 = poiData.addPOIitem(128.465247, 35.841210, "달성보", markerId, 0);
		item28.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item29 = poiData.addPOIitem(128.360861, 35.592618, "합천창녕보", markerId, 0);
		item29.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item30 = poiData.addPOIitem(128.550693, 35.377190, "창녕함안보", markerId, 0);
		item30.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item31 = poiData.addPOIitem(128.977554, 35.311124, "양산 물문회관", markerId, 0);
		item31.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item32 = poiData.addPOIitem(128.947856, 35.108891, "낙동강 하구둑", markerId, 0);
		item32.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item33 = poiData.addPOIitem(127.482187, 36.474474, "대청댐", markerId, 0);
		item33.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item34 = poiData.addPOIitem(127.259444, 36.476320, "세종보", markerId, 0);
		item34.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item35 = poiData.addPOIitem(127.097097, 36.464658, "공주보", markerId, 0);
		item35.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item36 = poiData.addPOIitem(126.940335, 36.317439, "백제보", markerId, 0);
		item36.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item37 = poiData.addPOIitem(126.921513, 36.130105, "익산 성당포구", markerId, 0);
		item37.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item38 = poiData.addPOIitem(126.742528, 36.022816, "금강하구둑", markerId, 0);
		item38.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item39 = poiData.addPOIitem(128.003136, 36.767739, "행촌교차로", markerId, 0);
		item39.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item40 = poiData.addPOIitem(127.820040, 36.803798, "괴강교", markerId, 0);
		item40.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item41 = poiData.addPOIitem(127.582774, 36.787400, "백로공원", markerId, 0);
		item41.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item42 = poiData.addPOIitem(127.446019, 36.678583, "무심천교", markerId, 0);
		item42.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item43 = poiData.addPOIitem(127.321085, 36.517407, "합강공원", markerId, 0);
		item43.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item44 = poiData.addPOIitem(127.018179, 35.371300, "담양댐", markerId, 0);
		item44.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item45 = poiData.addPOIitem(127.016861, 35.331862, "메타 세콰이아길", markerId, 0);
		item45.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item46 = poiData.addPOIitem(126.891642, 35.247134, "담양 대나무 숲", markerId, 0);
		item46.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item47 = poiData.addPOIitem(126.761477, 35.065315, "승촌보", markerId, 0);
		item47.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item48 = poiData.addPOIitem(126.626590, 34.971006, "죽산보", markerId, 0);
		item48.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item49 = poiData.addPOIitem(126.541839, 34.915029, "무안 느러지", markerId, 0);
		item49.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item50 = poiData.addPOIitem(126.445737, 34.801995, "영산강 하구둑", markerId, 0);
		item50.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item51 = poiData.addPOIitem(127.108872, 35.539712, "섬진강댐", markerId, 0);
		item51.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item52 = poiData.addPOIitem(127.201423, 35.460431, "장군목", markerId, 0);
		item52.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item53 = poiData.addPOIitem(127.188757, 35.338791, "향가유원지", markerId, 0);
		item53.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item54 = poiData.addPOIitem(127.327895, 35.303610, "횡탄정", markerId, 0);
		item54.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item55 = poiData.addPOIitem(127.470116, 35.181841, "사성암", markerId, 0);
		item55.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item56 = poiData.addPOIitem(127.621017, 35.185037, "남도대교", markerId, 0);
		item56.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item57 = poiData.addPOIitem(127.721738, 35.079410, "매화마을", markerId, 0);
		item57.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
		NMapPOIitem item58 = poiData.addPOIitem(127.761242, 34.959919, "배알도 수변공원", markerId, 0);
		item58.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		poiData.addPOIitem(127.022, 37.51, "Pizza 123-456", markerId, 0);
//		poiData.addPOIitem(127.023, 37.51, "Pizza 111-412", markerId, 0);
//		poiData.addPOIitem(127.024, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.025, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.026, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.027, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.028, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.029, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.030, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.031, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.032, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.033, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.034, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.035, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.036, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
//		poiData.addPOIitem(127.037, 37.51, "fdsfdszza 1fds11-4fsd12", markerId, 0);
		
		
		
		
		poiData.endPOIdata();

		
		
		
		
		
		
		
		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// set event listener to the overlay
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		// select an item
		poiDataOverlay.selectPOIitem(0, true);

		// show all POI data
		poiDataOverlay.showAllPOIdata(0);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/* Local Functions */

//	private void restoreInstanceState() {
//		mPreferences = getPreferences(MODE_PRIVATE);
//
//		int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
//		int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
//		int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
//		int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
//		boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
//		boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);
//
//		mMapController.setMapViewMode(viewMode);
//		mMapController.setMapViewTrafficMode(trafficMode);
//		mMapController.setMapViewBicycleMode(bicycleMode);
//		mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);
//	}

	
	
	private void saveInstanceState() {
		if (mPreferences == null) {
			return;
		}

		NGeoPoint center = mMapController.getMapCenter();
		int level = mMapController.getZoomLevel();
		int viewMode = mMapController.getMapViewMode();
		boolean trafficMode = mMapController.getMapViewTrafficMode();
		boolean bicycleMode = mMapController.getMapViewBicycleMode();

		SharedPreferences.Editor edit = mPreferences.edit();

		edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
		edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
		edit.putInt(KEY_ZOOM_LEVEL, level);
		edit.putInt(KEY_VIEW_MODE, viewMode);
		edit.putBoolean(KEY_TRAFFIC_MODE, trafficMode);
		edit.putBoolean(KEY_BICYCLE_MODE, bicycleMode);

		edit.commit();

	}
	
	
	
	
	
	

	
	

	
	
	// NMapDataProvider Listener   //지도에 생성할 데이터 관련
	private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

			if (DEBUG) {
				Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
					+ ((placeMark != null) ? placeMark.toString() : null));
			}

			if (errInfo != null) {
				Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

				Toast.makeText(MapActivity.this, errInfo.toString(), Toast.LENGTH_LONG).show();
				return;
			}

			if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
				mFloatingPOIdataOverlay.deselectFocusedPOIitem();

				if (placeMark != null) {
					mFloatingPOIitem.setTitle(placeMark.toString());
				}
				mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
			}
		}

	};
	
	
	
	/* MyLocation Listener */
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

			if (mMapController != null) {
				mMapController.animateTo(myLocation);
			}

			return true;
		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

			// stop location updating
			//			Runnable runnable = new Runnable() {
			//				public void run() {										
			//					stopMyLocation();
			//				}
			//			};
			//			runnable.run();	

			Toast.makeText(MapActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

			Toast.makeText(MapActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

			stopMyLocation();
		}

	};
	
	
	
	private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

		@Override
		public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

			if (overlayItem != null) {
				// [TEST] 말풍선 오버레이를 뷰로 설정함
				String title = overlayItem.getTitle();
				if (title != null && title.length() > 5) {
					return new com.jiam.touchriding.map.NMapCalloutCustomOverlayView(MapActivity.this, itemOverlay, overlayItem, itemBounds);
				}
			}

			// null을 반환하면 말풍선 오버레이를 표시하지 않음
			return null;
		}

	};
	
	private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

		@Override
		public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
			Rect itemBounds) {

			// handle overlapped items
			if (itemOverlay instanceof NMapPOIdataOverlay) {
				NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

				// check if it is selected by touch event
				if (!poiDataOverlay.isFocusedBySelectItem()) {
					int countOfOverlappedItems = 1;

					NMapPOIdata poiData = poiDataOverlay.getPOIdata();
					for (int i = 0; i < poiData.count(); i++) {
						NMapPOIitem poiItem = poiData.getPOIitem(i);

						// skip selected item
						if (poiItem == overlayItem) {
							continue;
						}

						// check if overlapped or not
						if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
							countOfOverlappedItems++;
						}
					}

					if (countOfOverlappedItems > 1) {
						String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
						Toast.makeText(MapActivity.this, text, Toast.LENGTH_LONG).show();
						return null;
					}
				}
			}

			// use custom old callout overlay
			if (overlayItem instanceof NMapPOIitem) {
				NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

				if (poiItem.showRightButton()) {
					return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
						mMapViewerResourceProvider);
				}
			}

			// use custom callout overlay
			return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

			// set basic callout overlay
			//return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);			
		}

	};


	

	
	
	
	// POI data State Change Listener// POI click 시 정보 표시
	private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

		@Override
		public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
			}

			//ppi 클릭시 ...
			// [[TEMP]] handle a click event of the callout
			Toast.makeText(MapActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				if (item != null) {
					Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
				} else {
					Log.i(LOG_TAG, "onFocusChanged: ");
				}
			}
		}
	};

	
	
	
	
	/*@Override
	public void onAnimationStateChange(NMapView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapCenterChange(NMapView arg0, NGeoPoint arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapInitHandler(NMapView arg0, NMapError arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub
		//Log.e("ddd", "FFF");
	}*/
	
	
	/** 
	 * Container view class to rotate map view.
	 */
	private class MapContainerView extends ViewGroup {

		public MapContainerView(Context context) {
			super(context);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			final int width = getWidth();
			final int height = getHeight();
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);
				final int childWidth = view.getMeasuredWidth();
				final int childHeight = view.getMeasuredHeight();
				final int childLeft = (width - childWidth) / 2;
				final int childTop = (height - childHeight) / 2;
				view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
			}

			if (changed) {
				mOverlayManager.onSizeChanged(width, height);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
			int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
			int sizeSpecWidth = widthMeasureSpec;
			int sizeSpecHeight = heightMeasureSpec;

			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);

				if (view instanceof NMapView) {
					if (mMapView.isAutoRotateEnabled()) {
						int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
						sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
						sizeSpecHeight = sizeSpecWidth;
					}
				}

				view.measure(sizeSpecWidth, sizeSpecHeight);
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	

	

}