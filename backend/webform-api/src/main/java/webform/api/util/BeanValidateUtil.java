package webform.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collector;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import webform.api.exception.ApiException;

public class BeanValidateUtil {
	/** Log */
	private static Log log = LogFactory.getLog(BeanValidateUtil.class);

	/**
	 * bean検証
	 * @param <T> entityクラス
	 * @param bean bean
	 */
	public static <T> void validateBean(T bean) {
		validateBean(bean, Default.class);
	}

	/**
	 * bean検証
	 * @param <T> entityクラス
	 * @param bean bean
	 * @param groups 制約グループ
	 */
	public static <T> void validateBean(T bean, Class<?>... groups) {
		Collector<ConstraintViolation<T>, StringJoiner, String> messageCollector
			= Collector.of(
				() -> new StringJoiner(", "),
				(j, cv) -> j.add(cv.getPropertyPath() + " " + cv.getMessage()),
				(j1, j2) -> j1.merge(j2),
				StringJoiner::toString
			);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(bean, groups);
		if (constraintViolations.size() > 0) {
			log.warn(constraintViolations.stream().collect(messageCollector));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", ApiException.CODE.PARAM_ERR.toString());
			map.put("title", "パラメータエラー");
			map.put("message", "入力情報が無効です。");
			throw new ApiException(map);
		}
	}
}
