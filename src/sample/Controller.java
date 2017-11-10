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
    private static final int  exterior_angles_sum = 360;
    private int polygon, level;
    private GraphicsContext gc;

    @FXML
    public Button btnSubmit;
    public Canvas canvas;
    public TextField edtPolygon;
    public TextField edtLevel;
    public Label txtHint;


    public void btnSubmitOnAction(){
        System.out.println("btnSubmitOnAction");

        verification();

        paint();
    }

    private void verification(){
        try {
            polygon = Integer.parseInt(edtPolygon.getText());
            txtHint.setText("");
        }catch (NumberFormatException e){
            System.out.println("NumberFormatException");
            edtPolygon.setText("3 ~ 6");
            txtHint.setText("Number Invalid!");
        }

        try {
            level = Integer.parseInt(edtLevel.getText());
            txtHint.setText("");
        }catch (NumberFormatException e){
            System.out.println("NumberFormatException");
            edtLevel.setText("0 ~ 5");
            txtHint.setText("Number Invalid!");
        }
    }

    private void paint(){
        gc = canvas.getGraphicsContext2D();
        gc.setLineCap(StrokeLineCap.BUTT);
        gc.setLineWidth(3);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        koch(MAX_LENGTH -50, 450,
                (MAX_LENGTH -100),180, level);
    }

    private void koch(double startX, double startY, double length,int angle, int leftLevel){
        if (leftLevel == -1){
            gc.strokeLine(startX, startY, startX + length * Math.sin(Math.toRadians(angle)), startY + length * Math.cos(angle));
        }else {
            koch(startX, startY, length/3, angle, leftLevel-1);
            double x = startX - length*2 /3;
            double y = startY;
            int degree = angle;
//            for (int i = 0; i < polygon - 2; i++){
//                koch(x, y, length/3, degree + exterior_angles_sum/polygon, leftLevel-1);
//                x += length/3 * Math.sin(degree);
//                y += length/3 * Math.cos(degree);
//                degree += exterior_angles_sum/polygon;
//            }
            koch(startX - length*2 /3, startY, length/3, angle, leftLevel-1);
        }
    }
}
