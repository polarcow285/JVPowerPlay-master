package org.firstinspires.ftc.teamcode.auto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.exception.RobotCoreException;
import org.firstinspires.ftc.teamcode.Projects.hi;
@Autonomous(name = "BasicAuto")
public class BasicAuto extends LinearOpMode{
    enum Parking{
        Right,
        Left
    }
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();
    public hi robot = new hi();

    @Override
    public void runOpMode() throws InterruptedException {
        //initialize hardware map
        robot.init(hardwareMap);
        ParkingAUTO.Parking a = ParkingAUTO.Parking.Right;

        int direction = 1;
        int otherDirection = -1;
        boolean isRight = true;

        while(!isStarted()){


                previousGamepad1.copy(currentGamepad1);
                currentGamepad1.copy(gamepad1);


            if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper){

                isRight = !isRight;
            }
            if (isRight){

                a = ParkingAUTO.Parking.Right;
            }
            else{

                a = ParkingAUTO.Parking.Left;
            }
            telemetry.addData("Parking",a);
            telemetry.update();

        }
        waitForStart(); //wait for play button to be pressed
        // autonomous happens here
        if (a == ParkingAUTO.Parking.Right){
            robot.fRightWheel.setPower(1);
            robot.bRightWheel.setPower(-1);
            robot.fLeftWheel.setPower(-1);
            robot.bLeftWheel.setPower(1);
            sleep(1000);
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(0);


        }
        else{
            robot.fRightWheel.setPower(-1);
            robot.bRightWheel.setPower(1);
            robot.fLeftWheel.setPower(1);
            robot.bLeftWheel.setPower(-1);
            sleep(1000);
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
            sleep(10);
            robot.fRightWheel.setPower(1);
            robot.bRightWheel.setPower(1);
            robot.fLeftWheel.setPower(1);
            robot.bLeftWheel.setPower(1);
            sleep(1000);
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
            sleep(10);
            robot.fRightWheel.setPower(1);
            robot.bRightWheel.setPower(1);
            robot.fLeftWheel.setPower(-1);
            robot.bLeftWheel.setPower(-1);
            sleep(500);
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
            sleep(10);
            robot.fRightWheel.setPower(-1);
            robot.bRightWheel.setPower(-1);
            robot.fLeftWheel.setPower(1);
            robot.bLeftWheel.setPower(1);
            sleep(500);
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
            sleep(10);
            robot.fRightWheel.setPower(-1);
            robot.bRightWheel.setPower(-1);
            robot.fLeftWheel.setPower(-1);
            robot.bLeftWheel.setPower(-1);
            sleep(1000);
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(0);


        }
        //while(robot.right.isBusy()|| robot.left.isBusy()) {

        //telemetry.addData("Status", robot.armMotor.getCurrentPosition());
        // telemetry.update();
        //}



    }
}
