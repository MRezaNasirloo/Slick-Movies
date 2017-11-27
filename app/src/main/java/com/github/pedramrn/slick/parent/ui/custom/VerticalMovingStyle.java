package com.github.pedramrn.slick.parent.ui.custom;

// import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;

/**
 * Copied from {@link com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle}
 */
public class VerticalMovingStyle {/*implements ScrollParallaxImageView.ParallaxStyle {

    private final float scale;

    public VerticalMovingStyle(float scale) {
        this.scale = scale;
    }

    public VerticalMovingStyle() {
        scale = 0.5f;
    }

    @Override
    public void onAttachedToImageView(ScrollParallaxImageView view) {
        // only supports CENTER_CROP
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onDetachedFromImageView(ScrollParallaxImageView view) {

    }

    @Override
    public void transform(ScrollParallaxImageView view, Canvas canvas, int x, int y) {
        if (view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            return;
        }

        // image's width and height
        int iWidth = view.getDrawable().getIntrinsicWidth();
        int iHeight = view.getDrawable().getIntrinsicHeight();
        if (iWidth <= 0 || iHeight <= 0) {
            return;
        }

        // view's width and height
        int vWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        int vHeight = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();

        // device's height
        int dHeight = view.getResources().getDisplayMetrics().heightPixels;

        if (iWidth * vHeight < iHeight * vWidth) {
            // avoid over scroll
            if (y < -vHeight) {
                y = -vHeight;
            } else if (y > dHeight) {
                y = dHeight;
            }

            float imgScale = (float) vWidth / (float) iWidth;
            float max_dy = Math.abs((iHeight * imgScale - vHeight) * scale);
            float translateY = -(2 * max_dy * y + max_dy * (vHeight - dHeight)) / (vHeight + dHeight);
            canvas.translate(0, translateY);
        }
    }
*/
}

