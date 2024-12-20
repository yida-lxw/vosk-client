package com.yida.constants;

/**
 * @author yida
 * @package com.yida.constants
 * @date 2024-12-18 11:57
 * @description Type your description over here.
 */
public class Constants {
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String DEFAULT_USER_TIMEZONE = "Asia/Shanghai";

	/**
	 * Http请求头：Content-Type
	 */
	public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";

	/**
	 * 缓冲区大小(单位:byte)
	 */
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

	/**
	 * 默认项目运行环境:dev
	 */
	public static final String DEFAULT_ENV = "dev";

	public static final String BOOLEAN_TRUE = "true";
	public static final String BOOLEAN_FALSE = "false";
	public static final String EMPTY_STRING = "";
	public static final String DOT = ".";
	public static final String ASTERISK = "*";
	public static final String HYPHEN = "-";
	public static final String UNDERSCORE = "_";
	public static final String POUNG_SIGN = "#";
	public static final String PERCENTAGE_SIGN = "%";
	public static final String POUNG_SIGN_UNICODE = "%23";

	//正斜杠
	public static final String FORWARD_SLASH = "/";
	//反斜杠
	public static final String BACK_SLASH = "\\";
	//反斜杠(用于Java正则表达式中)
	public static final String BACK_SLASH_IN_REGEX = "\\\\";

	public static final String ID_FIELD_NAME_DEFAULT = "id";

	//2G文件大小(单位:bytes)
	public static final long DEFAULT_2G_FILE_SIZE = 2147483648L;

	public static final long DEFAULT_KEEP_ALIVE_TIME = 60000L;

	/**
	 * CPU核数
	 */
	public static final int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors();

	/**
	 * 线程池默认最大线程数
	 */
	public static final int DEFAULT_MAX_POOL_SIZE = DEFAULT_CORE_SIZE * 2 + 1;

	/**
	 * 队列默认容量
	 */
	public static final int DEFAULT_QUEUE_CAPACITY = 1024;
	public static final int DEFAULT_MAX_RETRY_TIMES = 3;
	public static final int DEFAULT_RETRY_TIMES_MAP_SIZE = 1024;
	public static final String DEFAULT_THREAD_POOL_PREFIX = "Default-ThreadPool-";

	//默认保留4位小数
	public static final int DEFAULT_BIGDECIMAL_SCALE = 4;

	//保留2位小数
	public static final int DOUBLE_BIGDECIMAL_SCALE = 2;
}
