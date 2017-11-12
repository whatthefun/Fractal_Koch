package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.StrokeLineCap;

public class Controller {

    private static final double MAX_LENGTH = 600;
    private static final int exterior_angles_sum = 360; //外角和
    private int polygon, level;
    private GraphicsContext gc;

    @FXML
    public Button btnSubmit;
    public Canvas canvas;
    public TextField edtPolygon;
    public TextField edtLevel;
    public Label txtHint;


    public void btnSubmitOnAction() {
        System.out.println("btnSubmitOnAction");

        if (verification()){
            paint();
        }
    }

    //查驗使用者輸入值
    private boolean verification() {
        try {
            polygon = Integer.parseInt(edtPolygon.getText());
            if (polygon < 3){
                throw new NumberFormatException();
            }

            level = Integer.parseInt(edtLevel.getText());
            if (level < -1){
                throw new NumberFormatException();
            }

            txtHint.setText("");

            return true;
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException");
            edtPolygon.setText("3 ~ 6");
            edtLevel.setText("0 ~ 5");
            txtHint.setText("Number Invalid!");

            return false;
        }
    }

    //設定畫筆，並呼叫koch跑迴圈
    private void paint() {
        gc = canvas.getGraphicsContext2D();
        gc.setLineCap(StrokeLineCap.BUTT);
        gc.setLineWidth(1);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        koch(MAX_LENGTH - 50, 450,
                MAX_LENGTH - 100, -90, level, 1);
    }

    /*
    * 參數為座標、長度、角度、剩幾階、順/逆時針
    * 若是最後一階就利用座標、長度、角度算出終點，並劃出來。
    * 否則，將直線分成三個等分，一三段直接繼續遞迴
    * 第二段要做一個正多變形，而第一個邊不用畫
    * 每次都要轉(外角和/邊)度。
    * 而下一次會轉向，所以將 * */
    private void koch(double startX, double startY, double length, int angle, int leftLevel, int clockwise) {
        if (leftLevel == -1) { //termination condition
            gc.strokeLine(startX, startY, startX + length * Math.sin(Math.toRadians(angle)),
                    startY + length * Math.cos(Math.toRadians(angle)));
        } else {
            koch(startX, startY, length / 3, angle, leftLevel - 1, clockwise);//第一段
            double x = startX + length / 3 * Math.sin(Math.toRadians(angle)); //第二點位置
            double y = startY + length / 3 * Math.cos(Math.toRadians(angle));
            int degree = angle;
            if (clockwise < 0) {
                for (int i = 0; i < polygon - 1; i++) {
                    x += length / 3 * Math.sin(Math.toRadians(degree));
                    y += length / 3 * Math.cos(Math.toRadians(degree));
                    degree += exterior_angles_sum / polygon;
                    koch(x, y, length / 3, degree, leftLevel - 1, clockwise * -1);
                }
            } else {
                for (int i = 0; i < polygon - 1; i++) {
                    x += length / 3 * Math.sin(Math.toRadians(degree));
                    y += length / 3 * Math.cos(Math.toRadians(degree));
                    degree -= exterior_angles_sum / polygon;
                    koch(x, y, length / 3, degree, leftLevel - 1, clockwise * -1);
                }
            }
            koch(startX + length * 2 / 3 * Math.sin(Math.toRadians(angle)),
                    startY + length * 2 / 3 * Math.cos(Math.toRadians(angle)),
                    length / 3, angle, leftLevel - 1, clockwise);//第三段
        }
    }
}
