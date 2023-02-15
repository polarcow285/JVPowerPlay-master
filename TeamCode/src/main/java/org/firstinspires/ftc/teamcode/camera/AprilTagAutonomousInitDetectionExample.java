/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Projects.hi;
import org.firstinspires.ftc.teamcode.auto.ParkingAUTO;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


import java.util.ArrayList;

@Autonomous(name = "camera")
public class AprilTagAutonomousInitDetectionExample<tagOfInterest> extends LinearOpMode
{

    enum Side{
        Right,
        Left
    }
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();
    public hi robot = new hi();
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

   // Tag ID 1,2,3 from the 36h11 family
    int Left = 1;
    int Middle = 2;
    int Right = 3;
    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {

        Side a = Side.Right;
        boolean isRight = true;

        robot.init(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */

        while (!isStarted() && !isStopRequested())
        {

                previousGamepad1.copy(currentGamepad1);
                currentGamepad1.copy(gamepad1);



            robot.rightLift.setTargetPosition(0);
            robot.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.leftLift.setTargetPosition(0);
            robot.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)

                    if(tag.id == Left  || tag.id == Middle || tag.id == Right )
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest\nlol, get better:(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen in the history of this run, the records must be incomplete)");
                }
                else
                {
                    telemetry.addLine("\nBut we thankfully HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }



        if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper){

            isRight = !isRight;
        }
        if (isRight){

            a = Side.Right;
        }
        else{

            a = Side.Left;
        }
        telemetry.addData("Side",a);
        telemetry.update();

        if (a == Side.Right) {
            if(tagOfInterest == null || tagOfInterest.id == Left ) {
                //trajectory
                robot.armServo1.setPosition(0.15);
                robot.armServo2.setPosition(0.85);
                sleep(500);
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                sleep(500);
                moveRobot(1800,1);
                sleep(1500);
                robot.rightLift.setPower(-1);
                robot.leftLift.setPower(-1);
                robot.rightLift.setTargetPosition(-2000);
                robot.leftLift.setTargetPosition(-2000);
                sleep(5000);
                turnRobot("right", 89);
                sleep(1000);


                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                sleep(3000);
                moveRobot(200,-1);
                sleep(100);
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
                robot.rightLift.setTargetPosition(0);
                robot.leftLift.setTargetPosition(0);
                turnRobot("left",267);
                moveRobot(800,1);







            } else if(tagOfInterest.id == Middle) {
                //trajectory
                robot.armServo1.setPosition(0.15);
                robot.armServo2.setPosition(0.85);
                sleep(500);
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                sleep(500);
                moveRobot(1800,1);
                sleep(1500);
                robot.rightLift.setPower(-1);
                robot.leftLift.setPower(-1);
                robot.rightLift.setTargetPosition(-2000);
                robot.leftLift.setTargetPosition(-2000);
                sleep(5000);
                turnRobot("right", 89);
                sleep(1000);


                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                sleep(3000);
                moveRobot(200,-1);
                sleep(100);
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
                robot.rightLift.setTargetPosition(0);
                robot.leftLift.setTargetPosition(0);
              //  robot.rightLift.setPower(-.5);
               // robot.leftLift.setPower(-.5);
              //  robot.rightLift.setTargetPosition(-1550);
              //  robot.leftLift.setTargetPosition(-1550);
              //  sleep(5000);
              //  turnRobot("left", 50);

             //   robot.fRightWheel.setPower(.5);
             //   robot.fLeftWheel.setPower(.5);
              //  robot.bRightWheel.setPower(.5);
              //  robot.bLeftWheel.setPower(.5);
              //  sleep(250);


               // robot.rightLift.setPower(.5);
               // robot.leftLift.setPower(.5);
               // robot.rightLift.setTargetPosition(0);
              //  robot.leftLift.setTargetPosition(0);
              //  moveRobot(400, -.5);
               // turnRobot("right", 50);

            }else {
                //trajectory



                robot.armServo1.setPosition(0.15);
                robot.armServo2.setPosition(0.85);
                sleep(500);
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                sleep(500);
                moveRobot(1800,1);
                sleep(1500);
                robot.rightLift.setPower(-1);
                robot.leftLift.setPower(-1);
                robot.rightLift.setTargetPosition(-2000);
                robot.leftLift.setTargetPosition(-2000);
                sleep(5000);
                turnRobot("right", 89);
                sleep(1000);


                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                sleep(3000);
                moveRobot(200,-1);
                sleep(100);
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
                robot.rightLift.setTargetPosition(0);
                robot.leftLift.setTargetPosition(0);
                turnRobot("right",89);
                moveRobot(800,1);
            //    robot.rightLift.setPower(.5);
            //    robot.leftLift.setPower(.5);
             //   robot.rightLift.setTargetPosition(0);
             //   robot.leftLift.setTargetPosition(0);
             //   moveRobot(400, -.5);
              //  turnRobot("right", 50);
                //robot.rClaw.setPosition(1);
                //robot.lClaw.setPosition(0);
                //moveRobot(1600, 0.5);
             //   sleep(1000);
             //   turnRobot("right", 90);
             //   sleep(500);
              //  moveRobot(1600,.5);


                // robot.fRightWheel.setPower(-.8);
                //robot.fLeftWheel.setPower(.5);
                // robot.bRightWheel.setPower(.5);
                //robot.bLeftWheel.setPower(-.5);
                // sleep(2300);

            }
        }
        else {


            /* Actually do something useful */
            if (tagOfInterest == null || tagOfInterest.id == Left) {
                //trajectory


                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.armServo1.setPosition(0.15);
                robot.armServo2.setPosition(0.85);
                moveRobot(1500,1);
                robot.rightLift.setTargetPosition(-1800);
                robot.leftLift.setTargetPosition(-1800);
                sleep(200);
                turnRobot("right", 70);
                moveRobot(200,1);
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
                robot.rightLift.setTargetPosition(0);
                robot.leftLift.setTargetPosition(0);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);

             //   robot.rightLift.setPower(-.5);
               // robot.leftLift.setPower(-.5);
               // robot.rightLift.setTargetPosition(-1550);
              //  robot.leftLift.setTargetPosition(-1550);
              //  sleep(5000);
                turnRobot("left", 90);


             //   robot.rightLift.setPower(.5);
             //   robot.leftLift.setPower(.5);
             //   robot.rightLift.setTargetPosition(0);
             //   robot.leftLift.setTargetPosition(0);
              //  moveRobot(400, -.5);
             //   turnRobot("left", 50);
               // robot.rClaw.setPosition(1);
                //robot.lClaw.setPosition(0);
              //  moveRobot(1600, 0.5);
               // sleep(1000);
              //  turnRobot("left", 90);
              //  sleep(500);
              //  moveRobot(1600,.5);

                // robot.fRightWheel.setPower(.3);
                // robot.fLeftWheel.setPower(-.5);
                // robot.bRightWheel.setPower(-.75);
                // robot.bLeftWheel.setPower(.5);
                // sleep(2300);


            } else if (tagOfInterest.id == Middle) {
                //trajectory

                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.armServo1.setPosition(0.15);
                robot.armServo2.setPosition(0.85);
                moveRobot(1500,1);
                robot.rightLift.setTargetPosition(-1500);
                robot.leftLift.setTargetPosition(-1500);
                sleep(200);
                turnRobot("right", 70);
                moveRobot(200,1);
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
                robot.rightLift.setTargetPosition(0);
                robot.leftLift.setTargetPosition(0);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);

               // robot.rightLift.setPower(-.5);
               // robot.leftLift.setPower(-.5);
               // robot.rightLift.setTargetPosition(-1550);
              //  robot.leftLift.setTargetPosition(-1550);
              //  sleep(5000);
                //turnRobot("right", 50);

             //   robot.fRightWheel.setPower(.5);
             //   robot.fLeftWheel.setPower(.5);
             //   robot.bRightWheel.setPower(.5);
            //    robot.bLeftWheel.setPower(.5);
            //    sleep(250);
            //    robot.fRightWheel.setPower(0);
             //   robot.fLeftWheel.setPower(0);
             //   robot.bRightWheel.setPower(0);
            //    robot.bLeftWheel.setPower(0);
            //    sleep(3000);
            //    robot.rClaw.setPosition(0);
             //   robot.lClaw.setPosition(1);
             //   sleep(1000);

               // robot.rightLift.setPower(.5);
               // robot.leftLift.setPower(.5);
              //  robot.rightLift.setTargetPosition(0);
              //  robot.leftLift.setTargetPosition(0);
              //  moveRobot(400, -.5);
              //  turnRobot("left", 50);


            } else {
                //trajectory



                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.armServo1.setPosition(0.15);
                robot.armServo2.setPosition(0.85);
                moveRobot(1500,1);
                robot.rightLift.setTargetPosition(-1500);
                robot.leftLift.setTargetPosition(-1500);
                sleep(200);
                turnRobot("right", 70);
                moveRobot(200,1);
                robot.rightLift.setPower(1);
                robot.leftLift.setPower(1);
                robot.rightLift.setTargetPosition(0);
                robot.leftLift.setTargetPosition(0);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
               // robot.rightLift.setPower(-.5);
               // robot.leftLift.setPower(-.5);
              //  robot.rightLift.setTargetPosition(-1550);
              //  robot.leftLift.setTargetPosition(-1550);
              //  sleep(5000);
                turnRobot("right", 90);



              //  robot.rightLift.setPower(.5);
             //   robot.leftLift.setPower(.5);
              //  robot.rightLift.setTargetPosition(0);
              //  robot.leftLift.setTargetPosition(0);
              //  moveRobot(400, -.5);
              //  turnRobot("left", 50);
               // robot.rClaw.setPosition(1);
                //robot.lClaw.setPosition(0);
                //moveRobot(1600, 0.5);
               // sleep(1000);
              //  turnRobot("right", 90);
              //  sleep(500);
              //  moveRobot(1600,.5);


                // robot.fRightWheel.setPower(-.8);
                //robot.fLeftWheel.setPower(.5);
                // robot.bRightWheel.setPower(.5);
                //robot.bLeftWheel.setPower(-.5);
                // sleep(2300);

            }
        }
        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {sleep(20);}
    }


    void moveRobot(double time, double speed) {
        robot.fRightWheel.setPower(speed);
        robot.fLeftWheel.setPower(speed);
        robot.bRightWheel.setPower(speed);
        robot.bLeftWheel.setPower(speed);
        sleep((long) (time));
        robot.fRightWheel.setPower(0);
        robot.fLeftWheel.setPower(0);
        robot.bRightWheel.setPower(0);
        robot.bLeftWheel.setPower(0);
    }

    void turnRobot(String direction, int degrees) {
        if (direction == "right") {
            robot.fRightWheel.setPower(-.5);
            robot.bRightWheel.setPower(-.5);
            robot.fLeftWheel.setPower(.5);
            robot.bLeftWheel.setPower(.5);
            sleep(degrees/45*500);
            robot.fRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
        }
        if (direction == "left") {
            robot.fRightWheel.setPower(.5);
            robot.bRightWheel.setPower(.5);
            robot.fLeftWheel.setPower(-.5);
            robot.bLeftWheel.setPower(-.5);
            sleep(degrees/45*650);
            robot.fRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
        }
    }
    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}