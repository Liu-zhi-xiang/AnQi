package com.motorbike.anqi.init;

import com.baidu.location.BDLocation;

/**
 * @author lzx
 * @date 2017/12/28
 * @info
 */

public  class BaseRequesUrl {

//    public static final String IPs = "http://172.20.10.8:8080";
    public static final String IPs = "http://c23664916f.51mypc.cn:33741";
    public static String uesrId="";
    public static String uesrName="";
    public static String account="";
    public static String uesrRoomNo="";//我的房间号
    public static String XroomNo="";//准备加的房间号
    public static String roomNmID="";//我加入的房间号
    public static String uesrCity="";
    public static String jpushId="";
    public static String roomNo="0";//记录推送行程邀请的房间号
    public static String newRuning;//
    public static boolean wangyiServer =false;//网易云链接
    public static boolean RongIMLj =false;//融云链接
    public static String RoomGL="0";//0不是管理员，1是管理员
    public static String jingyin="1";//0静音（话筒可用，喇叭禁止），1不静音
    public static String uesrHead=IPs+"/img/user/avatar/007.png";
//    public static BDLocation melocation=null;
    public static boolean mode=false;
    public static final String IP = IPs + "/anqi";
    public static final String register = IP + "/api/service/register";//注册
    public static final String login = IP + "/api/service/login";//登录
    public static final String GETCODE = IP + "/api/service/sendmessage";//获取验证码
    public static final String FORGETPASSWORD = IP + "/api/service/updateuserpassword";//找回密码
    public static final String PersonalCenter = IP + "/api/service/personalcenter";//个人中心接口
    public static final String LevelInfo = IP + "/api/service/levelinfo";//等级详情
    public static final String bangdingphone = IP + "/api/service/bindphone";//绑定手机号
    public static final String EditoUserInfo = IP + "/api/service/editoruserinfo";//编辑个人信息
    public static final String Mycollect = IP + "/api/service/mycollectroom";//我的收藏
    public static final String FeedBack = IP + "/api/service/suggestions";//意见反馈
    public static final String IntegralMall = IP + "/api/service/integralmall";//积分商城
    public static final String MsgList = IP + "/api/service/systemmessage";//系统消息列表
    public static final String MsgInfo = IP + "/api/service/systemmessageInfo";//系统消息详情
    public static final String ChangeDetail = IP + "/api/service/exchangedetails";//兑换详情
    public static final String IntegralExchange = IP + "/api/service/integralexchange";//积分兑换
    public static final String Aboutwe = IP + "/h5/about.jsp";//关于我们
    public static final String UserCarType = IP + "/api/service/usercartype";//获取用户已有车型列表
    public static final String GetALlBrand = IP + "/api/service/getAllBrand";//获取所有车型品牌
    public static final String BrandTypeModel = IP + "/api/service/getTypeByBrand";//根据品牌获取车型
    public static final String AddCar = IP + "/api/service/addcartype";//添加车型
    public static final String IndexPage = IP + "/api/service/indexpage";//首页
    public static final String TalkRoom = IP + "/api/service/talkroom";//聊天室
    public static final String CollectRoom = IP + "/api/service/roomcollect";//收藏房间
    public static final String RoomQRCode = IP + "/api/service/roomQRcode";//房间二维码
    public static final String Removember = IP + "/api/service/removemember";//剔除房间成员
    public static final String CarFriendList = IP + "/api/service/carfriendslist";//车友列表
    public static final String FriendList = IP + "/api/service/friendslist";//好友列表
    public static final String OtherInfo = IP + "/api/service/personalInfo";//他人详情页
    public static final String FollowFriends = IP + "/api/service/attentionfriends";//关注好友
    public static final String Barley = IP + "/api/service/updatemicrophone";//禁麦/取消禁麦
    public static final String SetAdmin = IP + "/api/service/setadmin";//设置管理员
    public static final String Exitroom = IP + "/api/service/exitroom";//退出房间
    public static final String MyDynamic = IP + "/api/service/mydynamic";//我的动态
//    public static final String TripTopIndex = IP + "/api/service/triptopIndex";//榜单首页数据获取
    public static final String TripTop = IP + "/api/service/triptop";//周,月榜单

    public static final String AllAddress = IP + "/api/service/alldeliveryAddr";//获取用户所有地址
    public static final String AddAddress = IP + "/api/service/adddeliveryAddr";//添加地址
    public static final String facheyouquan=IP+"/api/service/publishdynamic";//发布车友圈
    public static final String cheyouquan=IP+"/api/service/carfriendsNoteList";//车友圈
    public static final String CYQ_Zan=IP+"/api/service/dynamicpraise";//车友圈_赞
    public static final String CYQ_DETAILs=IP+"/api/service/dynamicdetails";//车友圈_详情
    public static final String CYQ_PINGLUN=IP+"/api/service/noteremark";//车友圈_评论
    public static final String CYQ_HF_PINGLUN=IP+"/api/service/notereply";//车友圈_回复评论
    public static final String CYQ_CITY_DTA=IP+"/api/service/carfriendsArea";//车友圈_地区选择



    public static final String DelAddress=IP+"/api/service/deletedeliveryAddr";//删除收货地址
    public static final String OwnTrip=IP+"/api/service/historytripOwn";//历史行程个人
    public static final String TeamTrip=IP+"/api/service/historytripTeam";//历史行程团队
    public static final String OwnTripInfo=IP+"/api/service/tripinfoOwn";//行程详情个人





    public static final String TeamTripInfo=IP+"/api/service/tripinfoTeam";//行程详情团队
    public static final String ModifyAddress=IP+"/api/service/updatedeliveryAddr";//修改收获地址
    public static final String SetAddrDeafult=IP+"/api/service/setAddrDeafult";//设置默认地址
    public static final String ExchangeHistory=IP+"/api/service/exchangehistory";//兑换记录
    public static final String IntegralRecord=IP+"/api/service/integralrecord";//积分记录
    public static final String GenerateInviteCode=IP+"/api/service/generateInviteCode";//邀请有奖
    public static final String DelUserBrand=IP+"/api/service/deleteUserbrand";//删除用户车型
    public static final String Loginout=IP+"/api/service/loginout";//退出登录
    public static final String AddRoom=IP+"/api/service/roomAddUser";//扫描二维码加入房间
    public static final String DynamicRoomUser=IP+"/api/service/dynamicRoomPerson";//动态获取聊天室成员
    public static final String CloseRoom=IP+"/api/service/closeroom";//关闭房间
    public static final String ExchangeDetails=IP+"/api/service/exchangedetails";//兑换详情

    public static final String openRoom=IP+"/api/service/openroom";//开启房间
    public static final String zhumaiRoom=IP+"/api/service/setroomMicrophone";//设置房间模式
    public static final String xiangqingRoom=IP+"/api/service/roominfo";//房间详情
    public static final String TripRecord=IP+"/api/service/recordTrip";//行程记录
    public static final String GuidePage=IP+"/api/service/guidePage";//引导页
    public static final String Rongyun=IP+"/api/service/getToken";//获取融云token
    public static final String DelMsg=IP+"/api/service/deleteSystemmessage";//删除系统消息
    public static final String UnReadMsg=IP+"/api/service/unReadSysMessage";//获取消息未读数
    public static final String UpdateUserCartype=IP+"/api/service/updateUserCartype";//首页下拉切换车型接口
    public static final String CheckCarSelect=IP+"/api/service/checkCarSelect";//查看每个人是否选择过车型接口
    public static final String StartTripMove=IP+"/api/service/startTripMove";//发起行程推送消息
    public static final String RoompopuMub=IP+"/api/service/getRoomPersonNum";//房间人数上限
    public static final String wangyiTocken=IP+"/api/service/getWangyiyunToken";//
    public static final String VerifyRoomPsd=IP+"/api/service/roomPasswordConfirm";//获取房间是否有密码验证
    public static final String friendsposition=IP+"/api/service/setcarfriendsposition";// 设置车友列表地理位置 (经纬度,地区)
    public static final String noteIsRemind=IP+"/api/service/noteIsRemind";// 车友圈是否红点展示
    public static final String SetDefaultCar=IP+"/api/service/setdefaultCartype";// 设置默认车型
    public static final String jubao=IP+"/api/service/notereport";// 举报车友圈
    public static final String xuigaiRoomINfo=IP+"/api/service/changeTripRoute";// 修改语音室信息
    public static final String RenZheng=IP+"/api/service/serialNoConfirm ";//认证
}