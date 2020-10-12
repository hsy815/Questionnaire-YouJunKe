package com.biotecan.questionnaire.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.transition.Slide
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.adapter.UserSettingAdapter
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.entity.UserSetting
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.pre.PreData
import com.biotecan.questionnaire.util.BitmapOrBase64
import com.biotecan.questionnaire.util.DateAndString
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import com.biotecan.questionnaire.view.CircleImageView
import com.biotecan.questionnaire.view.UserSettingDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.best_top_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription


class UserSettingActivity : BaseActivity(), View.OnClickListener {

    private var newUserInfoOpt: OptResultDTO<UserInfo>? = null
    private val MAIN_ACTIVITY: Int = 100
    private val SDK_PERMISSION_REQUEST = 127
    private val TAKE_PHOTO_REQUEST: Int = 111
    private val TAKE_ALBUM_REQUEST: Int = 222

    private lateinit var mSettingAdapter: UserSettingAdapter
    private var mUserInfo: UserInfo? = null
    private var mUserSettingList = ArrayList<UserSetting>()
    private lateinit var mUserSettingDialog: UserSettingDialog
    private lateinit var mUserSetting: UserSetting//点击头像返回的头像对象
    private lateinit var mImageView: CircleImageView//点击头像  返回的头像imageView

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_user_setting
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "个人信息"
        best_top_right.text = "保存"
        best_top_right.setTextColor(ContextCompat.getColor(this, R.color.l_p_wx))

        best_top_back.setOnClickListener(this)
        best_top_right.setOnClickListener(this)

        mUserInfo = MyApplication.myApp!!.appUserInfo
        if (mUserInfo == null) mUserInfo = PreData.getUserInfo()
        mUserSettingList = PreData.getUserSetting(mUserInfo!!)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        user_setting_cyc.layoutManager = layoutManager
        mSettingAdapter = UserSettingAdapter(this, mUserSettingList)
        user_setting_cyc.adapter = mSettingAdapter
        mUserSettingDialog = UserSettingDialog(this)
        mSettingAdapter.setOnItemClick(object : UserSettingAdapter.OnItemClick {
            override fun itemData(userSetting: UserSetting, textView: TextView) {
                mUserSettingDialog.clickOption(
                    userSetting.title,
                    userSetting.value,
                    object : UserSettingDialog.OnItemClick {
                        override fun itemValue(value: String) {
                            textView.text = value
                            userSetting.temporaryValue = valueIsGender(value)
                        }
                    })
            }
        })
        mSettingAdapter.setOnHeadItemClick(object : UserSettingAdapter.OnHeadItemClick {
            override fun itemData(userSetting: UserSetting, imageView: CircleImageView) {
                mUserSetting = userSetting
                mImageView = imageView
                getPermission()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.best_top_back -> {
                back()
            }
            R.id.best_top_right -> {
                saveUserSetting()
            }
        }
    }

    private fun pictureManager() {
        AlertDialog.Builder(this)
            .setMessage("选择图片方式")
            .setNeutralButton("拍照") { dialog, _ ->
                isCamera()
                dialog.dismiss()
            }
            .setPositiveButton("相册") { dialog, _ ->
                isAlbum()
                dialog.dismiss()
            }.create().show()
    }

    /**
     * 打开相机拍照
     */
    private fun isCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST)
    }

    /**
     * 打开相册选择图片
     */
    private fun isAlbum() {
        //以下方式可以打开最近图片
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, TAKE_ALBUM_REQUEST)

        //以下方式只打开相册
//        val intent = Intent()
//        intent.action = Intent.ACTION_PICK
//        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        startActivityForResult(intent, 222)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO_REQUEST -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    return
                }
                val photo: Bitmap? = data!!.getParcelableExtra("data")
                mImageView.setImageBitmap(photo)
                val photo64 = BitmapOrBase64.bitmap2Base64(photo)
                mUserSettingList[0].temporaryValue = photo64!!
            }
            TAKE_ALBUM_REQUEST -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    return
                }
                val imgUri = data?.data
                mImageView.setImageURI(imgUri)
                val bitmap =
                    BitmapFactory.decodeStream(contentResolver.openInputStream(imgUri!!))
                val photo64 = BitmapOrBase64.bitmap2Base64(bitmap)
                mUserSettingList[0].temporaryValue = photo64!!
            }
        }
    }

    /**
     * 选择如果是性别   把性别转换为标识数字
     */
    private fun valueIsGender(string: String) = when (string) {
        "未知" -> "0"
        "男性" -> "1"
        "女性" -> "2"
        else -> string
    }

    /**
     * 返回处理
     */
    private fun back() {
        var str = verification()
        if (TextUtils.isEmpty(str)) {
            onBackPressed()
        } else {
            str = str.substring(0, str.length - 1)
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("$str 未保存是否退出")
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, _ ->
                    dialog.dismiss()
                    onBackPressed()
                }.show()
        }
    }

    /**
     * 点击返回时判断是否有修改
     */
    private fun verification(): String {
        var str: String = ""
        for (userSetting: UserSetting in mUserSettingList) {
            if (!TextUtils.isEmpty(userSetting.temporaryValue)
                && userSetting.value != userSetting.temporaryValue
            ) {
                str += "${userSetting.title},"
            }
        }
        return str
    }

    /**
     * 保存修改信息并发起提交
     */
    private fun saveUserSetting() {
        val map = HashMap<String, String>()
        for (userSetting: UserSetting in mUserSettingList) {
            if (!TextUtils.isEmpty(userSetting.temporaryValue)) {
                map[userSetting.fieldName] = userSetting.temporaryValue
            } else {
                if (!TextUtils.isEmpty(userSetting.value)) {
                    map[userSetting.fieldName] = valueIsGender(userSetting.value!!)
                }
            }
        }

        val son1 = Gson()
        val str = son1.toJson(map)
        val son2 = Gson()
        val newUserInfo = son2.fromJson(str, UserInfo::class.java)
        isUserInfo(newUserInfo)
    }

    /**
     * 判断必填数据是否完善
     */
    private fun isUserInfo(userInfo: UserInfo) {
        userInfo.Code = mUserInfo!!.Code
        userInfo.Kind = mUserInfo!!.Kind
        userInfo.BN = mUserInfo!!.BN
        if (TextUtils.isEmpty(userInfo.Name)) {
            if (TextUtils.isEmpty(mUserInfo!!.Name)) {
                MyToast.makeS(this, "请输入姓名")
                return
            } else {
                userInfo.Name = mUserInfo!!.Name
            }
        }

        if (0 == userInfo.Gender) {
            if (0 == mUserInfo!!.Gender) {
                MyToast.makeS(this, "请选择性别")
                return
            } else {
                userInfo.Gender = mUserInfo!!.Gender
            }
        }

        if (TextUtils.isEmpty(userInfo.MobilePhone)) {
            if (TextUtils.isEmpty(mUserInfo!!.MobilePhone)) {
                MyToast.makeS(this, "请输入手机号")
                return
            } else {
                userInfo.MobilePhone = mUserInfo!!.MobilePhone
            }
        }

        if (TextUtils.isEmpty(userInfo.DateOfBirth)) {
            if (TextUtils.isEmpty(mUserInfo!!.DateOfBirth)) {
                MyToast.makeS(this, "请输入出生日期")
                return
            } else {
                userInfo.DateOfBirth = mUserInfo!!.DateOfBirth
            }
        }

        userInfo.DateOfBirth = DateAndString.getDate2HH("${userInfo.DateOfBirth} 00:00:00")

        if (null == userInfo.Stature || 0 == userInfo.Stature.toInt()) {
            if (null == mUserInfo!!.Stature || 0 == mUserInfo!!.Stature.toInt()) {
                MyToast.makeS(this, "请输入当前身高")
                return
            } else {
                userInfo.Stature = mUserInfo!!.Stature
            }
        }

        if (null == userInfo.BodyWeight || 0 == userInfo.BodyWeight.toInt()) {
            if (null == mUserInfo!!.BodyWeight || 0 == mUserInfo!!.BodyWeight.toInt()) {
                MyToast.makeS(this, "请输入当前体重")
                return
            } else {
                userInfo.BodyWeight = mUserInfo!!.BodyWeight
            }
        }

        if (TextUtils.isEmpty(userInfo.ContactAddress)) {
            if (TextUtils.isEmpty(mUserInfo!!.ContactAddress)) {
                MyToast.makeS(this, "请输入联系地址")
                return
            } else {
                userInfo.ContactAddress = mUserInfo!!.ContactAddress
            }
        }

        upDataUserInfo(userInfo)
    }

    /**
     * 发起用户信息修改请求
     */
    private fun upDataUserInfo(str: UserInfo) {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription!!.add(DataManager.getUpDataInfo(str)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MySubscriber<OptResultDTO<UserInfo>>(this) {
                override fun onNext(t: OptResultDTO<UserInfo>?) {
                    newUserInfoOpt = t
                }

                override fun onError(e: Throwable?) {
                    super.onError(e)
                    loadingDialog.dismiss()
                }

                override fun onCompleted() {
                    newUserInfoManager()
                }
            }
            ))
    }

    /**
     * 用户信息修改请求反馈
     */
    private fun newUserInfoManager() {
        loadingDialog.dismiss()
        if (newUserInfoOpt == null) {
            MyToast.makeS(this, "修改失败,请重试!")
            return
        }
        if (newUserInfoOpt!!.OperatingStatus) {
            MyApplication.myApp!!.appUserInfo = newUserInfoOpt!!.DataResult
            goMainActivity()
        } else {
            MyToast.makeS(this, newUserInfoOpt!!.Message)
        }

    }

    /**
     * 修改用户信息后返回MainActivity需要MainActivity更新数据
     */
    private fun goMainActivity() {
        setResult(MAIN_ACTIVITY)
        onBackPressed()
    }

    @TargetApi(23)
    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions =
                java.util.ArrayList<String>()
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA)
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (permissions.size > 0) {
                requestPermissions(
                    permissions.toTypedArray(),
                    SDK_PERMISSION_REQUEST
                )
            } else {
                pictureManager()
            }
        } else {
            pictureManager()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN
            && KeyEvent.KEYCODE_BACK == keyCode
        ) {
            back()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
