package com.younle.younle624.myapplication.view;/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.LogUtils;

import java.lang.reflect.Field;

/**
 * A simple dialog containing an {@link DatePicker}.
 *
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 */
public class DoubleDatePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

	private static final String START_YEAR = "start_year";
	private static final String END_YEAR = "end_year";
	private static final String START_MONTH = "start_month";
	private static final String END_MONTH = "end_month";
	private static final String START_DAY = "start_day";
	private static final String END_DAY = "end_day";

	private final DatePicker mDatePicker_start;
	private final DatePicker mDatePicker_end;
	private final OnDateSetListener mCallBack;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {


		void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                       DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth, int startHour, int startMinute, int endHour, int endMinute);
		void Cancel(DoubleDatePickerDialog doubleDatePickerDialog);

		void startPrint(DatePicker mDatePicker_start, int year, int month, int dayOfMonth, DatePicker mDatePicker_end, int year1, int month1, int dayOfMonth1, int startHour, int startMinte, int endHour, int endMinute);



//		boolean onJudgeDate(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
//					   DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth);
	}

	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	/*public DoubleDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}*/

	/*public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
								  int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth, true);
	}*/

	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param theme
	 *            the theme to apply to this dialog
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 */
	public DoubleDatePickerDialog(Context context, int theme, final OnDateSetListener callBack,
								  String startDate, String endDate,
								  int startHour, int startMinute, int endHour, int endMinute,
								  boolean isDayVisible, boolean showPrint) {
		super(context, theme);

		mCallBack = callBack;

		Context themeContext = getContext();

		// setButton(BUTTON_POSITIVE,
		// themeContext.getText(android.R.string.date_time_done), this);
		setIcon(0);
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setView(view);
		Button print_total = (Button)view.findViewById(R.id.print_total);
		TextView tv_timedia_title = (TextView) view.findViewById(R.id.tv_timedia_title);
		View line1 = view.findViewById(R.id.line1);
		View line3 = view.findViewById(R.id.line3);
		LinearLayout ll_pnselector = (LinearLayout) view.findViewById(R.id.pn_selector);

		mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
		mDatePicker_end = (DatePicker) view.findViewById(R.id.datePickerEnd);
		final TimePicker tp_start= (TimePicker) view.findViewById(R.id.tp_start);
		final TimePicker tp_end= (TimePicker) view.findViewById(R.id.tp_end);
		TextView tv_yes= (TextView) view.findViewById(R.id.tv_yes);
		TextView tv_no= (TextView) view.findViewById(R.id.tv_no);

		resetDpSize(mDatePicker_start);
		resetDpSize(mDatePicker_end);
		resetTpSize(tp_start);
		resetTpSize(tp_end);
		tp_start.setIs24HourView(true);
		tp_end.setIs24HourView(true);
//		resetSize(mDatePicker_end);

		String[] start = startDate.split("-");
		String[] end = endDate.split("-");
		mDatePicker_start.init(Integer.valueOf(start[0]), Integer.valueOf(start[1])-1, Integer.valueOf(start[2]), this);
		mDatePicker_end.init(Integer.valueOf(end[0]), Integer.valueOf(end[1])-1, Integer.valueOf(end[2]), this);

		tp_start.setCurrentHour(startHour);
		tp_start.setCurrentMinute(startMinute);

		tp_end.setCurrentHour(endHour);
		tp_end.setCurrentMinute(endMinute);
		if(showPrint) {
		    print_total.setVisibility(View.VISIBLE);
			tv_timedia_title.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
			line3.setVisibility(View.VISIBLE);
			ll_pnselector.setVisibility(View.GONE);
		}else {
			ll_pnselector.setVisibility(View.VISIBLE);
			print_total.setVisibility(View.GONE);
			line1.setVisibility(View.GONE);

			/*line3.setVisibility(View.GONE);
			print_total.setVisibility(View.GONE);
			tv_timedia_title.setVisibility(View.GONE);
			setButton(BUTTON_POSITIVE, "确定", this);
			setButton(BUTTON_NEGATIVE, "取消", this);*/
		}
		if (!isDayVisible) {
			hidDay(mDatePicker_start);
			hidDay(mDatePicker_end);
		}

		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		int width = this.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		int height = this.getWindow().getWindowManager().getDefaultDisplay().getHeight();
		LogUtils.Log("width=="+width);
		LogUtils.Log("height=="+height);
		params.width= (int) (width*1);
		params.height= (int) (height*0.88);
		this.getWindow().setAttributes(params);

		print_total.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mCallBack.startPrint(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth()+1,
						mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
						mDatePicker_end.getMonth()+1, mDatePicker_end.getDayOfMonth(),
						tp_start.getCurrentHour(),tp_start.getCurrentMinute(),tp_end.getCurrentHour(),tp_end.getCurrentMinute());
			}
		});
		tv_no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallBack.Cancel(DoubleDatePickerDialog.this);
			}
		});
		tv_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCallBack != null) {
					mDatePicker_start.clearFocus();
					mDatePicker_end.clearFocus();
					mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth()+1,
							mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
							mDatePicker_end.getMonth()+1, mDatePicker_end.getDayOfMonth(),
							tp_start.getCurrentHour(),tp_start.getCurrentMinute(),tp_end.getCurrentHour(),tp_end.getCurrentMinute());

				}
			}
		});

	}

	private static final String TAG = "DoubleDatePickerDialog";
	private void resetTpSize(TimePicker picker) {
		LinearLayout tpContainer= (LinearLayout) picker.getChildAt(0);
		recycleSet(tpContainer);
	}

	private void recycleSet(LinearLayout tpContainer) {
		for (int i = 0; i < tpContainer.getChildCount(); i++) {
			View childAt = tpContainer.getChildAt(i);
			if(childAt instanceof NumberPicker) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin=0;
				params.rightMargin=10;
				childAt.setLayoutParams(params);
			}else if(childAt instanceof LinearLayout){
				recycleSet((LinearLayout) childAt);
			}
		}
	}

	private void resetDpSize(DatePicker picker) {
		LinearLayout dpContainier= (LinearLayout) picker.getChildAt(0);
		LinearLayout dpSpinner= (LinearLayout) dpContainier.getChildAt(0);
		int childCount = dpSpinner.getChildCount();
		for (int i = 0; i < childCount; i++) {
			NumberPicker np= (NumberPicker) dpSpinner.getChildAt(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin=0;
			params.rightMargin=10;
			np.setLayoutParams(params);
		}
	}

	/**
	 *
	 * @param mDatePicker
	 */
	private void hidDay(DatePicker mDatePicker) {
		Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
		for (Field datePickerField : datePickerfFields) {
			if ("mDaySpinner".equals(datePickerField.getName())) {
				datePickerField.setAccessible(true);
				Object dayPicker = new Object();
				try {
					dayPicker = datePickerField.get(mDatePicker);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				// datePicker.getCalendarView().setVisibility(View.GONE);
				((View) dayPicker).setVisibility(View.GONE);
			}
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		// Log.d(this.getClass().getSimpleName(), String.format("which:%d",
		// which));
		if (which == BUTTON_POSITIVE){
			tryNotifyDateSet();
		}else if(which==BUTTON_NEGATIVE) {
		    mCallBack.Cancel(this);
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		if (view.getId() == R.id.datePickerStart)
			mDatePicker_start.init(year, month, day, this);
		if (view.getId() == R.id.datePickerEnd)
			mDatePicker_end.init(year, month, day, this);
		// updateTitle(year, month, day);
	}

	/**
	 * ��ÿ�ʼ���ڵ�DatePicker
	 *
	 * @return The calendar view.
	 */
	public DatePicker getDatePickerStart() {
		return mDatePicker_start;
	}

	/**
	 * ��ý������ڵ�DatePicker
	 *
	 * @return The calendar view.
	 */
	public DatePicker getDatePickerEnd() {
		return mDatePicker_end;
	}

	/**
	 * Sets the start date.
	 *
	 * @param year
	 *            The date year.
	 * @param monthOfYear
	 *            The date month.
	 * @param dayOfMonth
	 *            The date day of month.
	 */
	public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
	}

	/**
	 * Sets the end date.
	 *
	 * @param year
	 *            The date year.
	 * @param monthOfYear
	 *            The date month.
	 * @param dayOfMonth
	 *            The date day of month.
	 */
	public void updateEndDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker_end.updateDate(year, monthOfYear, dayOfMonth);
	}

	private void tryNotifyDateSet() {
		/*if (mCallBack != null) {
			mDatePicker_start.clearFocus();
			mDatePicker_end.clearFocus();
//			mCallBack.onJudgeDate(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
//					mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
//					mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth());
			mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
					mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
					mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth());

		}*/
	}

	@Override
	protected void onStop() {
		// tryNotifyDateSet();
		super.onStop();
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(START_YEAR, mDatePicker_start.getYear());
		state.putInt(START_MONTH, mDatePicker_start.getMonth());
		state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
		state.putInt(END_YEAR, mDatePicker_end.getYear());
		state.putInt(END_MONTH, mDatePicker_end.getMonth());
		state.putInt(END_DAY, mDatePicker_end.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int start_year = savedInstanceState.getInt(START_YEAR);
		int start_month = savedInstanceState.getInt(START_MONTH);
		int start_day = savedInstanceState.getInt(START_DAY);
		mDatePicker_start.init(start_year, start_month, start_day, this);

		int end_year = savedInstanceState.getInt(END_YEAR);
		int end_month = savedInstanceState.getInt(END_MONTH);
		int end_day = savedInstanceState.getInt(END_DAY);
		mDatePicker_end.init(end_year, end_month, end_day, this);

	}
}
