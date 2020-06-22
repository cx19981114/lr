package cn.lr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * <p>使用方法:</p>
 * <pre>
 *		LoggerUtil.LOGGER.info(msg);
 *		LoggerUtil.LOGGER.debug(msg);
 *		LoggerUtil.LOGGER.warn(msg);
 *		LoggerUtil.LOGGER.error(msg);
 * </pre>
 * 
 */
public class LoggerUtil {
	public static final Logger LOGGER = LoggerFactory.getLogger("root");

}
