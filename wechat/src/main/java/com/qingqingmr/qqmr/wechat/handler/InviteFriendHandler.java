package com.qingqingmr.qqmr.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.constant.WechatConstant;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.QRCodeUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.bean.InviteMemberVO;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.*;
import com.qingqingmr.qqmr.wechat.common.WechatProperties;
import com.qingqingmr.qqmr.wechat.common.util.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 邀请好友
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午6:53:27
 */
@Component
public class InviteFriendHandler {
	private static final Logger LOG = LoggerFactory.getLogger(InviteFriendHandler.class);

	@Autowired
	private SpringContextProperties springContextProperties;
	@Autowired
	private WechatProperties properties;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private UserBonusService userBonusService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private StoreAreaService storeAreaService;
	@Autowired
	private StoreService storeService;

	/**
	 * <p>
	 * 查询我的邀请码
	 * </p>
	 *
	 * @param parameters
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月5日 下午8:03:38
	 */
	public String getInviteCode(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		User user = userService.getUserById(userId);
		if (user == null) {
			json.put("code", -1);
			json.put("msg", "用户信息错误");
			return FastJsonUtil.toJsonString(json);
		}
		if (null != user && user.getRoleType() == RoleTypeEnum.CUSTOMER.code) {
			json.put("code", -1);
			json.put("msg", "您还不是会员，请先升级为会员");
			return FastJsonUtil.toJsonString(json);
		}

		Map<String, String> map = new HashMap<>();
		map.put("OPT", String.valueOf(WechatConstant.WECHAT_WX_CODE_QRCODE));
		map.put("userId", signId);
		json.put("wxCodeUrl", Security.wxGatewayBuildUrl(map, springContextProperties.getAppMd5Key()));

		json.put("inviteCode", user.getMobile());
		json.put("code", 1);
		json.put("msg", "查询邀请码成功");
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * <p>
	 * 生成邀请二维码
	 * </p>
	 *
	 * @param response
	 * @param parameters
	 * @return
	 * @throws Exception
	 * @author liujingjing
	 * @datetime 2018年7月6日 上午11:28:35
	 */
	public String inviteQRCode(HttpServletResponse response, Map<String, String> parameters) throws Exception {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());

		UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
		if (userInfo == null) {
			json.put("code", -1);
			json.put("msg", "用户信息有误");
			return FastJsonUtil.toJsonString(json);
		}
		Store store = storeService.getStoreById(userInfo.getStoreId());
		StoreArea storeArea = storeAreaService.getStoreAreaById(store.getAreaId());

//		String url = "pages/binding/binding";
		String url = "https://wechat.qingqingmr.com/yXUngpfWII.txt";
		Map<String, String> params = new HashMap<>();
		params.put("inviteCode", userInfo.getMobile());
		params.put("storeId", String.valueOf(userInfo.getStoreId()));
		params.put("cityId", String.valueOf(storeArea.getId()));

		ByteArrayOutputStream outputStream = QRCodeUtil.generateQRCode(url, params);
		if (outputStream == null) {
			json.put("code", -1);
			json.put("msg", "二维码查看失败");
			return FastJsonUtil.toJsonString(json);
		}

		InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
		byte[] bytes = new byte[is.available()];
		is.read(bytes);
		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();

		return "image";
	}

	/**
	 * <p>
	 * 分页查询用户佣金明细
	 * </p>
	 *
	 * @param parameters
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月6日 下午5:38:34
	 */
	public String listUserBonusPage(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		String currentPageStr = parameters.get("currPage");
		String pageNumStr = parameters.get("pageSize");

		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());

		if (!StrUtil.isNumeric(currentPageStr) || !StrUtil.isNumeric(pageNumStr)) {
			json.put("code", -1);
			json.put("msg", "分页参数不正确");
			return FastJsonUtil.toJsonString(json);
		}

		int currPage = Integer.parseInt(currentPageStr);
		int pageSize = Integer.parseInt(pageNumStr);

		currPage = currPage < 1 ? 1 : currPage;
		pageSize = pageSize < 1 ? 10 : pageSize;
		PageResult<UserBonus> pageResult = userBonusService.listUserBonusPage(currPage, pageSize, userId);
		json.put("code", 1);
		json.put("msg", "查询成功");
		json.put("page", pageResult.getPage());
		json.put("totalCount", pageResult.getTotalCount());
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * <p>
	 * 分页查询用户邀请会员列表
	 * </p>
	 *
	 * @param parameters
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月6日 下午7:53:33
	 */
	public String listInviteMemberPage(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		String typeStr = parameters.get("type");
		String currentPageStr = parameters.get("currPage");
		String pageNumStr = parameters.get("pageSize");

		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());

		if (StringUtils.isBlank(typeStr)) {
			json.put("code", -1);
			json.put("msg", "邀请的人员类型有误");
			return FastJsonUtil.toJsonString(json);
		}
		int type = Integer.parseInt(typeStr);
		if (!StrUtil.isNumeric(currentPageStr) || !StrUtil.isNumeric(pageNumStr)) {
			json.put("code", -1);
			json.put("msg", "分页参数不正确");
			return FastJsonUtil.toJsonString(json);
		}

		int currPage = Integer.parseInt(currentPageStr);
		int pageSize = Integer.parseInt(pageNumStr);

		currPage = currPage < 1 ? 1 : currPage;
		pageSize = pageSize < 1 ? 10 : pageSize;

		PageResult<InviteMemberVO> pageResult = userService.listInviteMemberPage(currPage, pageSize, userId, type);
		json.put("page", pageResult.getPage());
		json.put("totalCount", pageResult.getTotalCount());
		json.put("code", 1);
		json.put("msg", "查询成功");
		return FastJsonUtil.toJsonString(json);

	}

	/**
	 * <p>
	 * 查询我的邀请信息
	 * </p>
	 *
	 * @param parameters
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月9日 上午11:30:41
	 */
	public String myInvite(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		ResultInfo resultInfo = userBonusService.myInvite(userId);
		json.put("resultInfo", resultInfo);
		json.put("code", 1);
		json.put("msg", "查询成功");
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * 微信二维码
	 *
	 * @param response
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public String wxCodeQRCode(HttpServletResponse response, Map<String, String> parameters) throws Exception {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());

		UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
		if (userInfo == null) {
			json.put("code", -1);
			json.put("msg", "用户信息有误");
			return FastJsonUtil.toJsonString(json);
		}
		Store store = storeService.getStoreById(userInfo.getStoreId());
		StoreArea storeArea = storeAreaService.getStoreAreaById(store.getAreaId());
		
		StringBuffer text = new StringBuffer();
		text.append(userInfo.getMobile()).append("_").append(userInfo.getStoreId()).append("_").append(storeArea.getId());

		String key = springContextProperties.getProfile() + SystemRedisKeyConstant.ACCESS_TOKEN
				+ springContextProperties.getMchAppId();
		String value = redisTemplate.opsForValue().get(key);
		if (StringUtils.isBlank(value)) {
			String accessToken = WechatUtil.getAccessToken(properties.getAccessTokenUrl(),
					springContextProperties.getMchAppId(), springContextProperties.getAppSecret(),
					properties.getAccessTokenGrantType());
			JSONObject jsonObject = FastJsonUtil.parseObj(accessToken, JSONObject.class);
			if (jsonObject.containsKey("errcode")) {
				json.put("code", -1);
				json.put("msg", jsonObject.getString("errmsg"));
				return FastJsonUtil.toJsonString(json);
			}
			value = jsonObject.getString("access_token");
			redisTemplate.opsForValue().set(key, value, jsonObject.getInteger("expires_in"), TimeUnit.SECONDS);
		}

		byte[] bytes = WechatUtil.getWxCode(properties.getWxCodeUnLimitUrl(), value, text.toString(), "");
		if (bytes == null || bytes.length < 1) {
			json.put("code", -1);
			json.put("msg", "生成图片失败");
			return FastJsonUtil.toJsonString(json);
		}

		String str = new String(bytes, "UTF-8");
		if (str == null || str.startsWith("{")) {
			LOG.info("生成图片失败信息--{}", str);
			json.put("code", -1);
			json.put("msg", "生成图片失败");
			return FastJsonUtil.toJsonString(json);
		}

		response.setContentType("image/jpg;charset=utf-8");
		response.addHeader("Content-Disposition", "attachment;filename=wxqrcode.jpg");
		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
		return "image";
	}
}
