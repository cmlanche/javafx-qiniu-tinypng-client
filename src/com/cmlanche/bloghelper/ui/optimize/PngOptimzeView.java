package com.cmlanche.bloghelper.ui.optimize;

import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.ui.common.CommonDialogLayoutView;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.cmlanche.bloghelper.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by cmlanche on 2017/12/10.
 * png & jpg optimize view
 */
public class PngOptimzeView extends CommonDialogLayoutView {

    @FXML
    TextField widthField;
    @FXML
    TextField heightField;
    @FXML
    Label predictSize;

    private BucketFile bucketFile;

    public PngOptimzeView(BucketFile bucketFile) {
        this.bucketFile = bucketFile;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();

        String size = BucketUtils.getPictureSize(bucketFile);
        if (size.contains("x")) {
            String[] wh = size.split("x");
            widthField.setText(wh[0]);
            heightField.setText(wh[1]);
        }

        predictSize.setText(Utils.getSizeName((long) (bucketFile.getSize() * 0.75)));
    }

    @Override
    public String getTitle() {
        return "图片优化";
    }

    @Override
    protected void onOk() {
        String size = BucketUtils.getPictureSize(bucketFile);

        boolean isuse = true;
        int nw = 0, nh = 0;
        if (size.contains("x")) {
            String[] wh = size.split("x");
            if (wh[0].equals(widthField.getText()) && wh[1].equals(heightField.getText())) {
                isuse = false;
            } else {
                nw = Integer.parseInt(widthField.getText());
                nh = Integer.parseInt(heightField.getText());
            }
        } else {
            if (StringUtils.isNoneEmpty(widthField.getText())) {
                nw = -1;
            } else {
                nw = Integer.parseInt(widthField.getText());
            }
            if (StringUtils.isNoneEmpty(heightField.getText())) {
                nh = -1;
            } else {
                nh = Integer.parseInt(heightField.getText());
            }

            if (nw == -1 && nh == -1) {
                isuse = false;
            }
        }

        // 构造返回的数据
        PngOptimzeResponse response = new PngOptimzeResponse();
        response.setUse(isuse);
        response.setHeight(nw);
        response.setHeight(nh);

        ok(response);
    }

    @Override
    protected void onCancel() {
        cancel();
    }
}
