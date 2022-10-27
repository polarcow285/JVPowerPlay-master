package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Projects.hi;

public class TestTeleop extends LinearOpMode {
    public hi  robot = new hi();


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        int liftstart = 0;
        int liftend = 200;
        robot.lift.setTargetPosition(0);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        boolean isSpinning = false;

        while (opModeIsActive()) {

            if (gamepad1.a==true&&isSpinning == false){
                robot.intake.setPower(1);
                isSpinning = true;


            }
            else if (gamepad1.a == true&&isSpinning == true){
                robot.intake.setPower(0);
                isSpinning = false;

            }
            if (gamepad1.b==true&&isSpinning == false){
                robot.intake.setPower(-1);
                isSpinning = true;


            }
            else if (gamepad1.b == true&&isSpinning == true){
                robot.intake.setPower(0);
                isSpinning = false;

            }

            if (gamepad1.dpad_up == true){


                robot.lift.setPower(1);
                robot.lift.setTargetPosition(liftend);
            }

            if (gamepad1.dpad_down == true){

                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(liftstart);

            }
            if(robot.lift.getTargetPosition()>200||robot.lift.getTargetPosition() < 0){

                robot.lift.setPower(0);
            }
            telemetry.addData("Lift Encoder Count", robot.lift.getCurrentPosition());
            telemetry.update();
/*
            // move the arms up and down
            if (gamepad2.left_stick_y == true) {
                robot.liftArmMotorThing.setPower(-gamepad2.left_stick_y);
            }

            // moves the manipulator (manipulatorMotor)
            if (gamepad2.a == true) {
                robot.manipulatorMotor.setPower(.2);
            }
            if (gamepad2.y == true) {
                robot.manipulatorMotor.setPower(-.2);
            }



            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

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
//


