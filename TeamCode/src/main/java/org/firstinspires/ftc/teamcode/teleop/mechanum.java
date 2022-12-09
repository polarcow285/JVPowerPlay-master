package org.firstinspires.ftc.teamcode.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.exception.RobotCoreException;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Projects.hi;
@TeleOp(name = "mechanum")
public class mechanum extends LinearOpMode {
    public hi robot = new hi();


    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.dpad_right == true ) {
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                //isSpinning = true;


            }
            // else{

            //robot.intake.setPower(0);
            //}
            if (gamepad1.dpad_left == true ) {
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                // isSpinning = true;


            }

            if (gamepad1.dpad_down == true) {


                robot.lift.setPower(1);
                // robot.lift.setTargetPosition(liftend);
               // if (noU<0) {


                  //  noU = noU + 10;
                //}
                //robot.lift.setPower(-1);
                //robot.lift.setTargetPosition(noU);
            }
            if (gamepad1.dpad_up == true) {

                robot.lift.setPower(-1);
                //robot.lift.setTargetPosition(liftstart);

               // if(noU>-5000){
                  //  noU = noU-10;
                //}
                //robot.lift.setPower(-1);
                //robot.lift.setTargetPosition(noU);



            }
            else{
                robot.lift.setPower(0);
            }

            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            robot.fLeftWheel.setPower(frontLeftPower);
            robot.bLeftWheel.setPower(backLeftPower);
            robot.fRightWheel.setPower(frontRightPower);
            robot.bRightWheel.setPower(backRightPower);
        }
    }
}
