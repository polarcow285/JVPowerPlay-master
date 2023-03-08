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
        int scissorPosition = 0;
        int leftPosition = 0;
        int[] positions;
        double speed = .9;
        robot.scissorLift.setTargetPosition(0);
        robot.scissorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.scissorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        waitForStart();
        boolean isSpinning = false;

        while (opModeIsActive()) {

            }


            if (gamepad2.dpad_right == true ) {
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                //isSpinning = true;


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

            if (gamepad1.left_trigger == 1&&scissorPosition<0) {

                scissorPosition += 5;
                robot.scissorLift.setPower(.4);
                robot.scissorLift.setTargetPosition(scissorPosition);

            }
            else if (gamepad1.right_trigger == 1&&scissorPosition > -1800) {

                scissorPosition -= 5;
                robot.scissorLift.setPower(.7);
                robot.scissorLift.setTargetPosition(scissorPosition);




            }



            if (gamepad1.b == true){


                robot.rightLift.setTargetPosition(-350);
                robot.leftLift.setTargetPosition(-350);
                positions = WaitTillTargetReached(50, true);
                rightPosition = positions[0];
                leftPosition = positions[1];



           }
            else if (gamepad1.y == true){

                robot.rightLift.setTargetPosition(-850);
                robot.leftLift.setTargetPosition(-850);
                positions = WaitTillTargetReached(50, true);
                rightPosition = positions[0];
                leftPosition = positions[1];

            }

            else if(gamepad1.x == true){

                robot.rightLift.setTargetPosition(-1400);
                robot.leftLift.setTargetPosition(-1400);
                positions = WaitTillTargetReached(50, true);
                rightPosition = positions[0];
                leftPosition = positions[1];


            }
            else if (gamepad1.a == true){
              robot.rightLift.setTargetPosition(0);
              robot.leftLift.setTargetPosition(0);
              positions = WaitTillTargetReached(50, true);
              rightPosition = positions[0];
              leftPosition = positions[1];
              robot.rClaw.setPosition(0);
              robot.lClaw.setPosition(1);

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
    int[] WaitTillTargetReached(int tolerance, boolean lock){
        int scissorDifference = Math.abs(robot.scissorLift.getTargetPosition() - robot.scissorLift.getCurrentPosition());
        int check=102930293;
        while(scissorDifference > tolerance)

        {

            scissorDifference = Math.abs(robot.scissorLift.getTargetPosition() - robot.scissorLift.getCurrentPosition());
            if (robot.scissorLift.getCurrentPosition() == check) {
                break;
            }
            else {

                 check = robot.scissorLift.getCurrentPosition();
            }
            robot.scissorLift.setPower(-1);

            sleep(1);

        }

        int a = robot.scissorLift.getCurrentPosition();

        int[] positions = new int[] {a};


        if(!lock)
        {
            robot.scissorLift.setPower(0);
        }
        return(positions);
    }
    private void cycle() {

    }

}



