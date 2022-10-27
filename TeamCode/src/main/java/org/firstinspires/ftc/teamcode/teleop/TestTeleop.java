package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Projects.hi;
@TeleOp(name="TestTeleop")
public class TestTeleop extends LinearOpMode {
    public hi  robot = new hi();

    //enum SlowMode{
        //Slow,
      //  Fast
   // }
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        int liftstart = 0;
        int liftend = 200;
        double speed = 0;
        double urmom = 1;
        boolean isSpeed = true;
        boolean isSlow = false;
        //ParkingAUTO.Parking a = ParkingAUTO.Parking.Right;
       // robot.lift.setTargetPosition(0);
        //robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       // robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        boolean isSpinning = false;
        if (gamepad1.y == true && isSpeed == true){
            speed = 0.5;
            isSpeed = false;
        }
        if (gamepad1.y == true&& isSpeed == false){
            speed = 1;
            isSpeed =  true;
        }

        while (opModeIsActive()) {

            if(gamepad1.left_bumper==true&&isSlow == false){

                urmom = 0.5;
                isSlow = true;
            }
            if (gamepad1.left_bumper == true&&isSlow == true){
                urmom = 1;
                isSlow = false;
            }

            if (gamepad1.a==true&&isSpinning == false){
                //robot.intake.setPower(speed);
                isSpinning = true;


            }
            else if (gamepad1.a == true&&isSpinning == true){
               // robot.intake.setPower(0);
                isSpinning = false;

            }
            if (gamepad1.b==true&&isSpinning == false){
               // robot.intake.setPower(-speed);
                isSpinning = true;


            }
            else if (gamepad1.b == true&&isSpinning == true){
                //robot.intake.setPower(0);
                isSpinning = false;

            }

            if (gamepad1.dpad_up == true){


             //   robot.lift.setPower(1);
                //robot.lift.setTargetPosition(liftend);
            }

            if (gamepad1.dpad_down == true){

              //  robot.lift.setPower(-1);
              //  robot.lift.setTargetPosition(liftstart);

            }
           // if(robot.lift.getTargetPosition()>200||robot.lift.getTargetPosition() < 0){

                //robot.lift.setPower(0);
            //}
            //telemetry.addData("Lift Encoder Count", robot.lift.getCurrentPosition());
            //telemetry.update();





            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = ((y + x + rx) / denominator)*urmom;
            double backLeftPower = ((y - x + rx) / denominator)*urmom;
            double frontRightPower = ((y - x - rx) / denominator)*urmom;
            double backRightPower = ((y + x - rx) / denominator)*urmom;

            robot.fLeftWheel.setPower(frontLeftPower);
            robot.bLeftWheel.setPower(backLeftPower);
            robot.fRightWheel.setPower(frontRightPower);
            robot.bRightWheel.setPower(backRightPower);
        }
    }
}
//


