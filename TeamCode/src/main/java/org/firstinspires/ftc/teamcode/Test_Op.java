package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Basic: Test OpMode", group = "Test Opmode")

public class Test_Op extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor arm = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        arm  = hardwareMap.get(DcMotor.class, "arm");

        arm.setTargetPosition(arm.getCurrentPosition());
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setTargetPosition(1000);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            arm.setPower(1);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
        }

    }

}
