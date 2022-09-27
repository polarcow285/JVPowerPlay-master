package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Projects.hi;


@TeleOp(name="TestTeleop")
public class TestTeleop extends LinearOpMode {

    public hi robot = new hi();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {



        }
    }
}


