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
        boolean isRight = true;
        Side a = Side.Right;
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

        if (gamepad1.right_bumper){

            isRight = !isRight;

        }
        telemetry.addData("isRight", a);
        telemetry.update();

        if (isRight){

            a = Side.Right;
        }
        else{

            a = Side.Left;
        }
        telemetry.addData("Parking",a);
        telemetry.update();

        if (a == Side.Right) {
            if(tagOfInterest == null || tagOfInterest.id == Left ) {
                //trajectory
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                moveRobot(1.0, 0.5);
                sleep(1000);
                turnRobot("left", 45);
                sleep(500);
                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(-1400);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.lift.setPower(1);
                robot.lift.setTargetPosition(0);
                turnRobot("left", 45);
                moveRobot(.95,.5);
                // robot.fRightWheel.setPower(.3);
                // robot.fLeftWheel.setPower(-.5);
                // robot.bRightWheel.setPower(-.75);
                // robot.bLeftWheel.setPower(.5);
                // sleep(2300);

            } else if(tagOfInterest.id == Middle) {
                //trajectory
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                moveRobot(1,.5);
                sleep(1000);
                turnRobot("left", 45);
                sleep(500);
                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(-1400);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.lift.setPower(1);
                robot.lift.setTargetPosition(0);

            }else {
                //trajectory


                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                moveRobot(1,.5);
                turnRobot("left", 45);
                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(-1400);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.lift.setPower(1);
                robot.lift.setTargetPosition(0);
                turnRobot("right", 135);
                moveRobot(.95,.5);


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
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                moveRobot(1,.5);
                turnRobot("right",45);
                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(-1400);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.lift.setPower(1);
                robot.lift.setTargetPosition(0);
                turnRobot("left",135);
                moveRobot(1,.5);

                // robot.fRightWheel.setPower(.3);
                // robot.fLeftWheel.setPower(-.5);
                // robot.bRightWheel.setPower(-.75);
                // robot.bLeftWheel.setPower(.5);
                // sleep(2300);


            } else if (tagOfInterest.id == Middle) {
                //trajectory
                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                moveRobot(1,.5);
                turnRobot("right",45);
                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(-1400);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.lift.setPower(1);
                robot.lift.setTargetPosition(0);

            } else {
                //trajectory


                robot.rClaw.setPosition(1);
                robot.lClaw.setPosition(0);
                moveRobot(1,.5);
                turnRobot("right", 45);

                robot.lift.setPower(-1);
                robot.lift.setTargetPosition(-1400);
                robot.rClaw.setPosition(0);
                robot.lClaw.setPosition(1);
                robot.lift.setPower(1);
                robot.lift.setTargetPosition(0);
                turnRobot("right", 45);
                moveRobot(1,0.5);


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


    void moveRobot(double numOfTiles, double speed) {
        robot.fRightWheel.setPower(speed);
        robot.fLeftWheel.setPower(speed);
        robot.bRightWheel.setPower(speed);
        robot.bLeftWheel.setPower(speed);
        sleep((long) (1000/speed*numOfTiles));
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