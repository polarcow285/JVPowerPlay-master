package org.firstinspires.ftc.teamcode.Projects;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class hi extends Project{

    public DcMotor leftWheel = null;
    public DcMotor rightWheel = null;

    @Override
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        leftWheel = hwMap.dcMotor.get("leftWheel");
        rightWheel = hwMap.dcMotor.get("rightWheel");

        // Motors and facing in to each other
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        leftWheel.setDirection(DcMotor.Direction.REVERSE);

        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Stop();
    }

    public void Stop(){
        rightWheel.setPower(0);
        leftWheel.setPower(0);

    }
}
