package demo.binea.com.jumpbeans.widget;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by xubinggui on 6/6/15.
 */
public final class JumpingBeans {

	/**
	 * The default fraction of the whole animation time spent actually animating.
	 * The rest of the range will be spent in "resting" state.
	 * This the "duty cycle" of the jumping animation.
	 */
	public static final float DEFAULT_ANIMATION_DUTY_CYCLE = 0.65f;

	/**
	 * The default duration of a whole jumping animation loop, in milliseconds.
	 */
	public static final int DEFAULT_LOOP_DURATION = 1300;   // ms

	public static final String ELLIPSIS_GLYPH = "…";
	public static final String THREE_DOTS_ELLIPSIS = "...";
	public static final int THREE_DOTS_ELLIPSIS_LENGTH = 3;

	private JumpingBeansSpan[] jumpingBeans;
	private WeakReference<TextView> textView;

	private JumpingBeans(@NonNull JumpingBeansSpan[] beans, @NonNull TextView textView) {
		this.jumpingBeans = beans;
		this.textView = new WeakReference<>(textView);
	}

	/**
	 * Create an instance of the {@link Builder}
	 * applied to the provided {@code TextView}.
	 *
	 * @param textView The TextView to apply the JumpingBeans to
	 * @return the {@link Builder}
	 */
	public static Builder with(@NonNull TextView textView) {
		return new Builder(textView);
	}

	/**
	 * Stops the jumping animation and frees up the animations.
	 */
	public void stopJumping() {
		for (JumpingBeansSpan bean : jumpingBeans) {
			if (bean != null) {
				bean.teardown();
			}
		}

		cleanupSpansFrom(textView.get());
	}

	private static void cleanupSpansFrom(TextView tv) {
		if (tv != null) {
			CharSequence text = tv.getText();
			if (text instanceof Spanned) {
				CharSequence cleanText = removeJumpingBeansSpansFrom((Spanned) text);
				tv.setText(cleanText);
			}
		}
	}

	private static CharSequence removeJumpingBeansSpansFrom(Spanned text) {
		SpannableStringBuilder sbb = new SpannableStringBuilder(text.toString());
		Object[] spans = text.getSpans(0, text.length(), Object.class);
		for (Object span : spans) {
			if (!(span instanceof JumpingBeansSpan)) {
				sbb.setSpan(span, text.getSpanStart(span),
						text.getSpanEnd(span), text.getSpanFlags(span));
			}
		}
		return sbb;
	}

	private static CharSequence appendThreeDotsEllipsisTo(TextView textView) {
		CharSequence text = getTextSafe(textView);
		if (text.length() > 0 && endsWithEllipsisGlyph(text)) {
			text = text.subSequence(0, text.length() - 1);
		}

		if (!endsWithThreeEllipsisDots(text)) {
			text = new SpannableStringBuilder(text).append(THREE_DOTS_ELLIPSIS);  // Preserve spans in original text
		}
		return text;
	}

	private static CharSequence getTextSafe(TextView textView) {
		return !TextUtils.isEmpty(textView.getText()) ? textView.getText() : "";
	}

	private static boolean endsWithEllipsisGlyph(CharSequence text) {
		return TextUtils.equals(text.subSequence(text.length() - 1, text.length()), ELLIPSIS_GLYPH);
	}

	private static boolean endsWithThreeEllipsisDots(@NonNull CharSequence text) {
		if (text.length() < THREE_DOTS_ELLIPSIS_LENGTH) {
			// TODO we should try to normalize "invalid" ellipsis (e.g., ".." or "....")
			return false;
		}
		return TextUtils.equals(text.subSequence(text.length() - THREE_DOTS_ELLIPSIS_LENGTH, text.length()), THREE_DOTS_ELLIPSIS);
	}

	private static CharSequence ensureTextCanJump(int startPos, int endPos, CharSequence text) {
		if (text == null) {
			throw new NullPointerException("The textView text must not be null");
		}

		if (endPos < startPos) {
			throw new IllegalArgumentException("The start position must be smaller than the end position");
		}

		if (startPos < 0) {
			throw new IndexOutOfBoundsException("The start position must be non-negative");
		}

		if (endPos > text.length()) {
			throw new IndexOutOfBoundsException("The end position must be smaller than the text length");
		}
		return text;
	}

	/**
	 * Builder class for {@link JumpingBeans} objects.
	 * <p/>
	 * Provides a way to set the fields of a {@link JumpingBeans} and generate
	 * the desired jumping beans effect. With this builder you can easily append
	 * a Hangouts-style trio of jumping suspension points to any TextView, or
	 * apply the effect to any other subset of a TextView's text.
	 * <p/>
	 * <p>Example:
	 * <p/>
	 * <pre class="prettyprint">
	 * JumpingBeans jumpingBeans = JumpingBeans.with(myTextView)
	 *     .appendJumpingDots()
	 *     .setLoopDuration(1500)
	 *     .build();
	 * </pre>
	 */
	public static class Builder {

		private int startPos, endPos;
		private float animRange = DEFAULT_ANIMATION_DUTY_CYCLE;
		private int loopDuration = DEFAULT_LOOP_DURATION;
		private int waveCharDelay = -1;
		private CharSequence text;
		private TextView textView;
		private boolean wave;

		/*package*/ Builder(TextView textView) {
			this.textView = textView;
		}

		/**
		 * Appends three jumping dots to the end of a TextView text.
		 * <p/>
		 * This implies that the animation will by default be a wave.
		 * <p/>
		 * If the TextView has no text, the resulting TextView text will
		 * consist of the three dots only.
		 * <p/>
		 * The TextView text is cached to the current value at
		 * this time and set again in the {@link #build()} method, so any
		 * change to the TextView text done in the meantime will be lost.
		 * This means that <b>you should do all changes to the TextView text
		 * <i>before</i> you begin using this builder.</b>
		 * <p/>
		 * Call the {@link #build()} method once you're done to get the
		 * resulting {@link JumpingBeans}.
		 *
		 * @see #setIsWave(boolean)
		 */
		public Builder appendJumpingDots() {
			CharSequence text = appendThreeDotsEllipsisTo(textView);

			this.text = text;
			this.wave = true;
			this.startPos = text.length() - THREE_DOTS_ELLIPSIS_LENGTH;
			this.endPos = text.length();

			return this;
		}

		/**
		 * Appends three jumping dots to the end of a TextView text.
		 * <p/>
		 * This implies that the animation will by default be a wave.
		 * <p/>
		 * If the TextView has no text, the resulting TextView text will
		 * consist of the three dots only.
		 * <p/>
		 * The TextView text is cached to the current value at
		 * this time and set again in the {@link #build()} method, so any
		 * change to the TextView text done in the meantime will be lost.
		 * This means that <b>you should do all changes to the TextView text
		 * <i>before</i> you begin using this builder.</b>
		 * <p/>
		 * Call the {@link #build()} method once you're done to get the
		 * resulting {@link JumpingBeans}.
		 *
		 * @param startPos The position of the first character to animate
		 * @param endPos   The position after the one the animated range ends at
		 *                 (just like in {@link String#substring(int)})
		 * @see #setIsWave(boolean)
		 */
		public Builder makeTextJump(int startPos, int endPos) {
			CharSequence text = textView.getText();
			ensureTextCanJump(startPos, endPos, text);

			this.text = text;
			this.wave = true;
			this.startPos = startPos;
			this.endPos = endPos;

			return this;
		}

		/**
		 * Sets the fraction of the animation loop time spent actually animating.
		 * The rest of the time will be spent "resting".
		 * The default value is
		 * {@link JumpingBeans#DEFAULT_ANIMATION_DUTY_CYCLE}.
		 *
		 * @param animatedRange The fraction of the animation loop time spent
		 *                      actually animating the characters
		 */
		public Builder setAnimatedDutyCycle(float animatedRange) {
			if (animatedRange <= 0f || animatedRange > 1f) {
				throw new IllegalArgumentException("The animated range must be in the (0, 1] range");
			}
			this.animRange = animatedRange;
			return this;
		}

		/**
		 * Sets the jumping loop duration. The default value is
		 * {@link JumpingBeans#DEFAULT_LOOP_DURATION}.
		 *
		 * @param loopDuration The jumping animation loop duration, in milliseconds
		 */
		public Builder setLoopDuration(int loopDuration) {
			if (loopDuration < 1) {
				throw new IllegalArgumentException("The loop duration must be bigger than zero");
			}
			this.loopDuration = loopDuration;
			return this;
		}

		/**
		 * Sets the delay for starting the animation of every single dot over the
		 * start of the previous one, in milliseconds. The default value is
		 * the loop length divided by three times the number of character animated
		 * by this instance of JumpingBeans.
		 * <p/>
		 * Only has a meaning when the animation is a wave.
		 *
		 * @param waveCharOffset The start delay for the animation of every single
		 *                       character over the previous one, in milliseconds
		 * @see #setIsWave(boolean)
		 */
		public Builder setWavePerCharDelay(int waveCharOffset) {
			if (waveCharOffset < 0) {
				throw new IllegalArgumentException("The wave char offset must be non-negative");
			}
			this.waveCharDelay = waveCharOffset;
			return this;
		}

		/**
		 * Sets a flag that determines if the characters will jump in a wave
		 * (i.e., with a delay between each other) or all at the same
		 * time.
		 *
		 * @param wave If true, the animation is going to be a wave; if
		 *             false, all characters will jump ay the same time
		 * @see #setWavePerCharDelay(int)
		 */
		public Builder setIsWave(boolean wave) {
			this.wave = wave;
			return this;
		}

		/**
		 * Combine all of the options that have been set and return a new
		 * {@link JumpingBeans} instance.
		 * <p/>
		 * Remember to call the {@link #stopJumping()} method once you're done
		 * using the JumpingBeans (that is, when you detach the TextView from
		 * the view tree, you hide it, or the parent Activity/Fragment goes in
		 * the paused status). This will allow to release the animations and
		 * free up memory and CPU that would be otherwise wasted.
		 */
		public JumpingBeans build() {
			SpannableStringBuilder sbb = new SpannableStringBuilder(text);
			JumpingBeansSpan[] spans;
			if (wave) {
				spans = getJumpingBeansSpans(sbb);
			} else {
				spans = buildSingleSpan(sbb);
			}

			textView.setText(sbb);
			return new JumpingBeans(spans, textView);
		}

		private JumpingBeansSpan[] getJumpingBeansSpans(SpannableStringBuilder sbb) {
			JumpingBeansSpan[] spans;
			if (waveCharDelay == -1) {
				waveCharDelay = loopDuration / (3 * (endPos - startPos));
			}

			spans = new JumpingBeansSpan[endPos - startPos];
			for (int pos = startPos; pos < endPos; pos++) {
				JumpingBeansSpan jumpingBean =
						new JumpingBeansSpan(textView, loopDuration, pos - startPos, waveCharDelay, animRange);
				sbb.setSpan(jumpingBean, pos, pos + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spans[pos - startPos] = jumpingBean;
			}
			return spans;
		}

		private JumpingBeansSpan[] buildSingleSpan(SpannableStringBuilder sbb) {
			JumpingBeansSpan[] spans;
			spans = new JumpingBeansSpan[]{new JumpingBeansSpan(textView, loopDuration, 0, 0, animRange)};
			sbb.setSpan(spans[0], startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			return spans;
		}

	}
}
