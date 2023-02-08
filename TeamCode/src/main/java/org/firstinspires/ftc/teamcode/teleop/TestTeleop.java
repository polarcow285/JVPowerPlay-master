package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.exception.RobotCoreException;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Projects.hi;
@TeleOp(name = "TestTeleop")
public class TestTeleop extends LinearOpMode {
    public hi robot = new hi();


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        int liftstart = 0;
        int liftend = 200;
        int noU = -5000;
        boolean isdown = true;
        double speed = .9;
        boolean isSlow = false;
        robot.rightLift.setTargetPosition(0);
        robot.leftLift.setTargetPosition(0);
        robot.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        boolean isSpinning = false;

        while (opModeIsActive()) {

            if (gamepad2.right_bumper == true){
                robot.wristServo.setPosition(0);
            }
            else if (gamepad2.left_bumper == true){
                robot.wristServo.setPosition(1);
            }

            if (gamepad2.a == true){
                robot.armServo1.setPosition(1);
                robot.armServo2.setPosition(0);
            }
            if(gamepad2.b == true){
                robot.armServo1.setPosition(.72);
                robot.armServo2.setPosition(.28);
            }
            if(gamepad2.y==true){
                robot.armServo1.setPosition(.28);
                robot.armServo2.setPosition(.72);
            }
            if(gamepad2.x==true){
                robot.armServo1.setPosition(0);
                robot.armServo2.setPosition(1);
            }


            if (gamepad2.dpad_right == true ) {
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                //isSpinning = true;


            }
            if(gamepad2.right_trigger == 1) {
                int height = -1500;
                for(int i = 0; i < 3; i++) {
                    // I like cats
                    robot.leftLift.setTargetPosition(height);
                    robot.rightLift.setTargetPosition(height);
                    robot.rClaw.setPosition(1);
                    robot.lClaw.setPosition(0);
                    robot.wristServo.setPosition(0);
                    robot.armServo1.setPosition(0.28);
                    robot.armServo2.setPosition(0.72);
                    robot.rClaw.setPosition(0);
                    robot.lClaw.setPosition(1);
                    robot.rClaw.setPosition(1);
                    robot.lClaw.setPosition(0);
                    robot.armServo1.setPosition(0.8);
                    robot.armServo2.setPosition(0.2);
                    robot.wristServo.setPosition(1);
                    robot.leftLift.setTargetPosition(-350);
                    robot.rightLift.setTargetPosition(-350);
                    robot.rClaw.setPosition(0);
                    robot.lClaw.setPosition(1);
                    height = height + 32;
                }
            }
           // else{

                //robot.intake.setPower(0);
            //}
            if (gamepad2.dpad_left == true ) {
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
               // isSpinning = true;


            }
            else{

                //robot.intake.setPower(0);
            }

            if (gamepad1.left_trigger == 1&&noU<0) {


              //robot.lift.setPower(1);
               // robot.lift.setTargetPosition(liftend);



                    noU = noU + 5;

                robot.rightLift.setPower(-.4);
                robot.leftLift.setPower(-.4);
                robot.rightLift.setTargetPosition(noU);
                robot.leftLift.setTargetPosition(noU);
            }
            if (gamepad1.right_trigger == 1&&noU>-2200) {

                //robot.lift.setPower(-1);
                //robot.lift.setTargetPosition(liftstart);


                   noU = noU-5;

                robot.rightLift.setPower(.4);
                robot.leftLift.setPower(.4);
               robot.rightLift.setTargetPosition(noU);
               robot.leftLift.setTargetPosition(noU);



            }

            else {
              // robot.lift.setPower(0);
           }

            if (gamepad1.b == true){
                robot.rightLift.setPower(-1);
                robot.leftLift.setPower(-1);

                robot.rightLift.setTargetPosition(-300);
                robot.leftLift.setTargetPosition(-300);
                noU=-300;


           }
            if (gamepad1.y == true){
                robot.rightLift.setPower(-1);
                robot.leftLift.setPower(-1);
                robot.rightLift.setTargetPosition(-750);
                robot.leftLift.setTargetPosition(-750);
               noU = -750;
            }

            if(gamepad1.x == true){
                robot.rightLift.setPower(-1);
                robot.leftLift.setPower(-1);
              robot.rightLift.setTargetPosition(-1005);
              robot.leftLift.setTargetPosition(-1005);
              noU = -1005;
            }
            if (gamepad1.a == true){
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
              robot.rightLift.setTargetPosition(0);
              robot.leftLift.setTargetPosition(0);
              robot.rClaw.setPosition(0);
              robot.lClaw.setPosition(1);
              noU = 0;
            }

            if (gamepad1.left_bumper == true){
                    speed = .5;

            }
            else{
                speed = .9;
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

            robot.fLeftWheel.setPower(frontLeftPower*speed);
            robot.bLeftWheel.setPower(backLeftPower*speed);
            robot.fRightWheel.setPower(frontRightPower*speed);
            robot.bRightWheel.setPower(backRightPower*speed);
            // if(robot.lift.getTargetPosition()>200||robot.lift.getTargetPosition() < 0){

            //robot.lift.setPower(0);
            //}
            //telemetry.addData("Lift Encoder Count", robot.lift.getCurrentPosition());
            // telemetry.update();

            // move the arms up and down
            //   if (gamepad2.left_stick_y == true) {
            //  robot.liftArmMotorThing.setPower(-gamepad2.left_stick_y);
        }

        // moves the manipulator (manipulatorMotor)
        // if (gamepad2.a == true) {
        //  robot.manipulatorMotor.setPower(.2);
        //   }
        //   if (gamepad2.y == true) {
        //     robot.manipulatorMotor.setPower(-.2);
        //  }

    }

    private void cycle() {

    }

}



