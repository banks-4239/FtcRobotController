/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.util.Convert;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Basic: Auto OpMode", group = "Auto Opmode")
//@Disabled
public class BasicOpMode_Auto extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor robotArm = null;
    private DcMotor intake = null;
    private DcMotor spinner = null;

    double mmperin = 0.039701;

    double ticksperrotation = 537.7;
    double ticksperdegree = 1.49361111111;
    double wheeldiameter = 100;
    double pi = 3.1415;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFrontDrive = hardwareMap.get(DcMotor.class, "fl");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "fr");
        leftBackDrive = hardwareMap.get(DcMotor.class, "bl");
        rightBackDrive = hardwareMap.get(DcMotor.class, "br");
        robotArm = hardwareMap.get(DcMotor.class, "ra");
        intake = hardwareMap.get(DcMotor.class, "ra");
        spinner = hardwareMap.get(DcMotor.class, "sc");

        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        spinner.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        robotArm.setDirection(DcMotor.Direction.REVERSE);

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinner.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotArm.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        telemetry.addData("DcMotors", "Starting at %7d :%7d :%7d :%7d",
                rightFrontDrive.getCurrentPosition(),
                leftFrontDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition());
        //spinner.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)


        // Setup a variable for each drive wheel to save power level for telemetry

        telemetry.addData("DcMotors", "Starting at %7d :%7d :%7d :%7d",
                rightFrontDrive.getCurrentPosition(),
                leftFrontDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition());
        //spinner.getCurrentPosition());
        telemetry.update();


        //enter autonomous scripting here//////////////////////////////////////////////////////////////////////////////////////////

        moveForward(24, 1);
        liftArm(50, 1);
        waitForDriveMotors();




        //end autonomous scripting here////////////////////////////////////////////////////////////////////////////////////////////

        while (opModeIsActive()) {
        }
        // leftFrontDrive.setPower((moveY + rotate + moveX) / 2);
        // rightFrontDrive.setPower((moveY - rotate - moveX) / 2);
        // rightBackDrive.setPower((0 - moveY - rotate + moveX) / 2);
        // leftBackDrive.setPower((0 - moveY + rotate - moveX) / 2);


        //spinner.setPower(rSpin - lSpin);


    }




    public int inchestoticks(double inches) {
        return (int) Math.round((inches * ticksperrotation) / (mmperin * wheeldiameter * pi));
    }

    public void moveForward(int inches, double speed) {

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(inchestoticks(inches));
        rightFrontDrive.setTargetPosition(inchestoticks(inches));
        leftBackDrive.setTargetPosition(inchestoticks(inches));
        rightBackDrive.setTargetPosition(inchestoticks(inches));

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);

    }

    public void liftArm(int degrees, double speed) {

        robotArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotArm.setTargetPosition(inchestoticks(degrees * ticksperdegree));


        robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotArm.setPower(speed);

    }

    public void moveRotate(int degrees, double speed) {

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(degrees * 12);
        rightFrontDrive.setTargetPosition(-degrees * 12);
        leftBackDrive.setTargetPosition(degrees * 12);
        rightBackDrive.setTargetPosition(-degrees * 12);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);


    }

    public void moveDiagonal(int inches, double speed){//WORK IN PROGRESS
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //leftFrontDrive.setTargetPosition(inchestoticks(inches));
        rightFrontDrive.setTargetPosition(-inchestoticks(inches));
        leftBackDrive.setTargetPosition(-inchestoticks(inches));
        //rightBackDrive.setTargetPosition(inchestoticks(inches));

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);
    }


    public void moveSideways(int inches, double speed) {

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(inchestoticks(inches));
        rightFrontDrive.setTargetPosition(-inchestoticks(inches));
        leftBackDrive.setTargetPosition(-inchestoticks(inches));
        rightBackDrive.setTargetPosition(inchestoticks(inches));

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);


    }

    public void takeIn(double speed){

        intake.setPower(speed);

    }

    public void takeOut(double speed){

        intake.setPower(-speed);

    }

    void intakeOff(){
        intake.setPower(0);
    }

    public void waitForDriveMotors() {

        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() || leftBackDrive.isBusy() || rightBackDrive.isBusy()) {
            telemetry.update();
        }
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }


    public void spinnerLeft(double speed) {
        spinner.setPower(-speed);
    }

    public void spinnerRight(double speed) {
        spinner.setPower(speed);
    }

    public void spinnerEnd(double speed) {
        spinner.setPower(0);
    }

}