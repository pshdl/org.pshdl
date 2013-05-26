package javax.annotation;

import java.lang.annotation.*;
import java.util.regex.*;

import javax.annotation.meta.*;

@Documented
@TypeQualifier(applicableTo = String.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchesPattern {
	@RegEx
	String value();

	int flags() default 0;

	static class Checker implements TypeQualifierValidator<MatchesPattern> {
		@Override
		public When forConstantValue(MatchesPattern annotation, Object value) {
			final Pattern p = Pattern.compile(annotation.value(), annotation.flags());
			if (p.matcher(((String) value)).matches())
				return When.ALWAYS;
			return When.NEVER;
		}

	}
}
