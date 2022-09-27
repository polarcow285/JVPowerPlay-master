package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Projects.hi;
@Autonomous(name = "ParkingAuto")

public class ParkingAUTO extends LinearOpMode{
    public hi robot = new hi();

    @Override
    public void runOpMode() throws InterruptedException {
        //initialize hardware map
        robot.init(hardwareMap);

        waitForStart(); //wait for play button to be pressed
        // autonomous happens here
        robot.fRightWheel.setPower(1);
        robot.bRightWheel.setPower(-1);
        robot.fLeftWheel.setPower(-1);
        robot.bLeftWheel.setPower(1);
        sleep(1000);
        robot.fRightWheel.setPower(0);
        robot.bRightWheel.setPower(0);
        robot.fLeftWheel.setPower(0);
        robot.bLeftWheel.setPower(0);
        //while(robot.right.isBusy()|| robot.left.isBusy()) {

        //telemetry.addData("Status", robot.armMotor.getCurrentPosition());
        // telemetry.update();
        //}



    }
}


