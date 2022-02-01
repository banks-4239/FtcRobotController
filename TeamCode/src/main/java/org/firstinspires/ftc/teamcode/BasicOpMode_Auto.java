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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


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

    RobotReference rb = new RobotReference();

    public int hubNum;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        int amountducks = 0;

        rb.init(hardwareMap);

        initVuforia();
        initTfod();

        if (rb.tfod != null) {
            rb.tfod.activate();

            rb.tfod.setZoom(1, 16.0/9.0);
        }

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).


        telemetry.addData("Please choose a mode!", "up - redDuck, right - redWarehouse, left - blueDuck, down - blueWarehouse");
        telemetry.update();

        while (!rb.choosingAuto) {




            telemetry.addData("Please choose a setting", "");
            if (rb.redOrBlue) {
                telemetry.addData("Red", rb.settings[rb.onSetting - 1]);
                telemetry.addData("Red", ">" + rb.settings[rb.onSetting]);
                telemetry.addData("Red", rb.settings[rb.onSetting + 1]);
            } else {
                telemetry.addData("Blue", rb.settings[rb.onSetting - 1]);
                telemetry.addData("Blue", ">" + rb.settings[rb.onSetting]);
                telemetry.addData("Blue", rb.settings[rb.onSetting + 1]);
            }

            telemetry.addData("hub", hubNum);

            if (gamepad1.dpad_up) {
                if (rb.onSetting > 1 && !rb.settingButtonDown1) {
                    rb.onSetting--;
                }
                rb.settingButtonDown1 = true;
            } else {
                rb.settingButtonDown1 = false;
            }

            if (gamepad1.dpad_down) {
                if (rb.onSetting < 4 && !rb.settingButtonDown2) {
                    rb.onSetting++;
                }
                rb.settingButtonDown2 = true;
            } else {
                rb.settingButtonDown2 = false;
            }

            if (gamepad1.dpad_left) {
                rb.redOrBlue = true;
            } else if (gamepad1.dpad_right) {
                rb.redOrBlue = false;
            }

            if (isStarted()) {
                if (rb.redOrBlue) {
                    rb.choosingAuto = true;
                    rb.autoMode = rb.onSetting;
                } else {
                    rb.choosingAuto = true;
                    rb.autoMode = rb.onSetting + 4;
                }
            }
            hubNum = getElement();
            telemetry.update();



        }
/*
        telemetry.clearAll();
        telemetry.addData("DcMotors", "Starting at %7d :%7d :%7d :%7d",
                rightFrontDrive.getCurrentPosition(),
                leftFrontDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition());
        telemetry.update();
*/
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        rb.runtime.reset();

        telemetry.addData("DcMotors", "Starting at %7d :%7d :%7d :%7d",
                rb.rightFrontDrive.getCurrentPosition(),
                rb.leftFrontDrive.getCurrentPosition(),
                rb.rightBackDrive.getCurrentPosition(),
                rb.leftBackDrive.getCurrentPosition());
        //spinner.getCurrentPosition());
        telemetry.update();

        //enter autonomous code here//////////////////////////////////////////////////////////////////////////////////////////
        switch (rb.autoMode) {
            case 1:
                redDuckWithFreight();
                break;
            case 2:
                redDuckWithNoFreight();
                break;
            case 3:
                redWarehouseWithFreight();
                break;
            case 4:
                redWarehouseWithNoFreight();
                break;
            case 5:
                blueDuckWithFreight();
                //flipHub();
                break;
            case 6:
                blueDuckWithNoFreight();
                //flipHub();
                break;
            case 7:
                blueWarehouseWithFreight();
                //flipHub();
                break;
            case 8:
                blueWarehouseWithNoFreight();
                //flipHub();
                break;
        }

        while(opModeIsActive());
    }

    public void redDuckWithFreight() {



        moveRight(13, rb.MEDIUM);
        waitForDriveMotorsFast();
        moveForward(-4.5, rb.MEDIUM);
        waitForDriveMotorsFast();
        moveRight(4.5, 0.1);
        waitForDriveMotorsFast();
        spinnerRed(rb.SPINNER_SPEED);
        sleep(rb.SPIN_DURATION - 500);
        spinnerEnd();
        moveForward(-8, rb.MEDIUM);
        waitForDriveMotorsFast();
        moveRight(8, 0.1);
        waitForDriveMotorsFast();
        moveForward(-16, rb.MEDIUM);
        waitForDriveMotorsFast();
        moveRight(-8, 0.1);
        waitForDriveMotorsFast();
        rotateRight(-90, rb.MEDIUM);
        waitForDriveMotors();

        switch(hubNum){//30 towards hub
            case 1:
                level1(30);
                break;
            case 2:
                level2(30);
                break;
            case 3:
                level3(30);
                break;
        }




        /*
        moveRight(-8, SLOW);
        waitForDriveMotors();
        moveForward(7, SLOW);
        waitForDriveMotors();
        moveForward(-35, SLOW);//
        waitForDriveMotors();
        rotateRight(-90, SLOW);
        waitForDriveMotors();
        switch(hubNum){
            case 1:
                level1();
                break;
            case 2:
                level2();
                break;
            case 3:
                level3();
                break;
        }
        moveForward(-25, SLOW);
        waitForDriveMotors();
        rotateRight(90, SLOW);
        waitForDriveMotors();
        moveForward(20, SLOW);
        waitForDriveMotors();
        moveRight(5, SLOW);

         */
    }

    public void redDuckWithNoFreight() {
        moveRight(13, rb.SLOW);
        waitForDriveMotors();
        rotateRight(-90, rb.SLOW);
        waitForDriveMotors();
        moveForward(4.5, rb.SLOW);
        waitForDriveMotors();
        moveRight(5, 0.1);
        waitForDriveMotors();
        spinnerRed(rb.SPINNER_SPEED);
        sleep(rb.SPIN_DURATION);
        spinnerEnd();
        moveBackward(21, rb.SLOW);
        waitForDriveMotors();
    }

    public void redWarehouseWithFreight() {
        //positioning to score
        moveRight(18,rb.MEDIUM);
        waitForDriveMotors();
        moveBackward(7,rb.MEDIUM);
        waitForDriveMotors();
        if(hubNum != 1) {
            rotateRight(180, rb.MEDIUM);
            waitForDriveMotors();
        }
        switch(hubNum){//30 towards hub
            case 1:
                level1(10);
                break;
            case 2:
                level2(8.5);
                break;
            case 3:
                level3(16);
                break;
        }
        //parking in the warehouse
        if(hubNum == 1){
            moveLeft(17, rb.MEDIUM);
            waitForDriveMotors();
            rotateRight( -80, rb.MEDIUM);
            waitForDriveMotors();
            moveRight(32, rb.SLOW);
            waitForDriveMotorsFast();
            moveForward(44, rb.MEDIUM);
            waitForDriveMotors();
            moveLeft(26, rb.MEDIUM);
            waitForDriveMotors();
        }else{
            moveRight(17, rb.MEDIUM);
            waitForDriveMotors();
            rotateRight( -80, rb.MEDIUM);
            waitForDriveMotors();
            moveLeft(32, rb.SLOW);
            waitForDriveMotorsFast();
            moveBackward(45, rb.MEDIUM);
            waitForDriveMotors();
            moveRight(26, rb.MEDIUM);
            waitForDriveMotors();
        }

    }

    public void redWarehouseWithNoFreight() {
//        liftArm(29, rb.LIFT_ARM_ROTATE_PWR); // was 10
        moveBackward(36, rb.FAST);
        waitForDriveMotors();
        moveRight(25, rb.SLOW);
        waitForDriveMotors();
        rotateRight(-90, rb.SLOW);
        waitForDriveMotors();
        moveLeft(25, rb.SLOW);
        waitForDriveMotors();
//        liftArm(-29, rb.LIFT_ARM_ROTATE_PWR); // was -10
    }

    public void blueDuckWithFreight() { // unfinished
        moveRight(-4.75, rb.SLOW);
        waitForDriveMotors();
        moveForward(5, rb.SLOW);
        waitForDriveMotors();
        spinnerBlue(rb.SPINNER_SPEED);
        sleep(rb.SPIN_DURATION);
        spinnerEnd();
        moveForward(-5, rb.SLOW);
        waitForDriveMotors();
        moveRight(6, rb.SLOW);
        waitForDriveMotorsFast();
        moveForward(-33, rb.SLOW);
        waitForDriveMotors();
        moveRight(-20, rb.SLOW);
        waitForDriveMotors();
        rotateRight(-90, rb.SLOW);
        waitForDriveMotors();
        moveRight(-10, rb.SLOW / 2);
        waitForDriveMotors();
        moveForward(7, rb.SLOW / 2);
        waitForDriveMotors();
        liftArm(rb.LIFT_3, rb.LIFT_ARM_ROTATE_PWR);
        sleep(3000);
        liftArm(-rb.LIFT_3, rb.LIFT_ARM_ROTATE_PWR);
        sleep(3000);
        moveRight(60, rb.SLOW);
        waitForDriveMotors();
    }

    public void blueDuckWithNoFreight() {
        moveRight(-4.75, rb.SLOW);
        waitForDriveMotors();
        moveForward(2.75, rb.SLOW);
        waitForDriveMotors();
        spinnerBlue(rb.SPINNER_SPEED);
        sleep(rb.SPIN_DURATION);
        spinnerEnd();
        moveForward(-6.5, rb.SLOW);
        waitForDriveMotors();
        moveRight(6, rb.SLOW);
        waitForDriveMotors();
        moveRight(-31, rb.SLOW);
        waitForDriveMotors();
        moveForward(18, rb.SLOW);
    }

    public void blueWarehouseWithFreight() {
        //positioning to score
        moveLeft(18,rb.MEDIUM);
        waitForDriveMotors();
        telemetry.addData("diditgo", hubNum);
        telemetry.update();
        moveBackward(7,rb.MEDIUM);
        waitForDriveMotors();
        if(hubNum != 3) {
            rotateRight(-180, rb.MEDIUM);
            waitForDriveMotors();
        }
        switch(hubNum){//these are switched to accommodate for blue
            case 1:
                level2(8.5);
                break;
            case 2:
                level3(16);
                break;
            case 3:
                level1(10);
                break;
        }
        //parking in the warehouse
        if(hubNum == 3){
            moveRight(17, rb.MEDIUM);
            waitForDriveMotors();
            rotateRight( 80, rb.MEDIUM);
            waitForDriveMotors();
            moveLeft(32, rb.SLOW);
            waitForDriveMotorsFast();
            moveForward(44, rb.MEDIUM);
            waitForDriveMotors();
            moveRight(26, rb.MEDIUM);
            waitForDriveMotors();
        }else{
            moveLeft(17, rb.MEDIUM);
            waitForDriveMotors();
            rotateRight( 80, rb.MEDIUM);
            waitForDriveMotors();
            moveRight(32, rb.SLOW);
            waitForDriveMotorsFast();
            moveBackward(45, rb.MEDIUM);
            waitForDriveMotors();
            moveLeft(26, rb.MEDIUM);
            waitForDriveMotors();
        }
    }

    public void blueWarehouseWithNoFreight() {
//        liftArm(29, rb.LIFT_ARM_ROTATE_PWR); // was 10
//        waitForDriveMotors();
        moveForward(-36, rb.SLOW);
        waitForDriveMotors();
        moveRight(-25, rb.SLOW);
        waitForDriveMotors();
        rotateRight(90, rb.SLOW);
        waitForDriveMotors();
        moveRight(25, rb.SLOW);
        waitForDriveMotors();
//        liftArm(-29, rb.LIFT_ARM_ROTATE_PWR); // was -10
    }


    public void level1(double inches){
        moveBackward(inches, rb.MEDIUM);
        waitForDriveMotors();
        //scoring
        liftArm(rb.LIFT_2,rb.LIFT_ARM_ROTATE_PWR);
        waitForArm();
        takeOut(1);
        sleep(500);
        intakeOff();
        liftArm(rb.LIFT_0,rb.LIFT_ARM_ROTATE_PWR);
        waitForArm();
    }

    

    public void level2(double inches){
        moveForward(inches, rb.MEDIUM);
        waitForDriveMotors();
        //scoring
        liftArm(rb.LIFT_6,rb.LIFT_ARM_ROTATE_PWR);
        waitForArm();
        takeOut(1);
        sleep(500);
        intakeOff();
        liftArm(rb.LIFT_0,rb.LIFT_ARM_ROTATE_PWR);
        waitForArm();
    }

    public void level3(double inches){
        moveForward(inches, rb.MEDIUM);
        waitForDriveMotors();
        //scoring
        liftArm(rb.LIFT_5,rb.LIFT_ARM_ROTATE_PWR);
        waitForArm();
        takeOut(1);
        sleep(500);
        intakeOff();
        liftArm(rb.LIFT_0,rb.LIFT_ARM_ROTATE_PWR);
        waitForArm();
    }


/*
    public void flipHub(){
        telemetry.addData("before",hubNum);
        if(hubNum == 2) {
            hubNum = 3;
        } else
        {
            if(hubNum == 3) {
                hubNum = 1;
            }else{
                hubNum = 2;
            }
        }
    }
*/
    public int inchestoticks(double inches) {
        return (int) Math.round((inches * rb.ticksperrotation) / (rb.mmperin * rb.wheeldiameter * rb.pi));
    }

    public void moveBackward(double inches, double speed) {
        moveForward(-inches, speed);
    }

    public void moveForward(double inches, double speed) {
        rb.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rb.leftFrontDrive.setTargetPosition(inchestoticks(48*inches/46));
        rb.rightFrontDrive.setTargetPosition(inchestoticks(48*inches/46));
        rb.leftBackDrive.setTargetPosition(inchestoticks(48*inches/46));
        rb.rightBackDrive.setTargetPosition(inchestoticks(48*inches/46));

        rb.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rb.leftFrontDrive.setPower(speed);
        rb.rightFrontDrive.setPower(speed);
        rb.leftBackDrive.setPower(speed);
        rb.rightBackDrive.setPower(speed);
    }

    public void liftArm(int ticks, double power) {
        rb.robotArm.setTargetPosition(ticks);
        rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.robotArm.setPower(power);
    }

    public void rotateRight(int degrees, double speed) {
        rb.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rb.leftFrontDrive.setTargetPosition(degrees * 36/5);
        rb.rightFrontDrive.setTargetPosition(-degrees * 36/5);
        rb.leftBackDrive.setTargetPosition(degrees * 36/5);
        rb.rightBackDrive.setTargetPosition(-degrees * 36/5);

        rb.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rb.leftFrontDrive.setPower(speed);
        rb.rightFrontDrive.setPower(speed);
        rb.leftBackDrive.setPower(speed);
        rb.rightBackDrive.setPower(speed);
    }

    public void moveDiagonal(double inches, double speed) { //WORK IN PROGRESS
        rb.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //leftFrontDrive.setTargetPosition(inchestoticks(inches));
        rb.rightFrontDrive.setTargetPosition(-inchestoticks(inches));
        rb.leftBackDrive.setTargetPosition(-inchestoticks(inches));
        //rightBackDrive.setTargetPosition(inchestoticks(inches));

        rb.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rb.leftFrontDrive.setPower(speed);
        rb.rightFrontDrive.setPower(speed);
        rb.leftBackDrive.setPower(speed);
        rb.rightBackDrive.setPower(speed);
    }
    public void moveLeft(double inches, double speed) {
        moveRight(-inches, speed);
    }
    public void moveRight(double inches, double speed) {
        rb.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rb.leftFrontDrive.setTargetPosition(inchestoticks(0 - ((48*48)*inches/(42*46.5))));
        rb.rightFrontDrive.setTargetPosition(-inchestoticks(0 - ((48*48)*inches/(42*46.5))));
        rb.leftBackDrive.setTargetPosition(-inchestoticks(0 - ((48*48)*inches/(42*46.5))));
        rb.rightBackDrive.setTargetPosition(inchestoticks(0 - ((48*48)*inches/(42*46.5))));

        rb.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rb.leftFrontDrive.setPower(speed);
        rb.rightFrontDrive.setPower(speed);
        rb.leftBackDrive.setPower(speed);
        rb.rightBackDrive.setPower(speed);
    }

    public void takeIn(double speed) {
        rb.intake.setPower(-speed);
    }

    public void takeOut(double speed) {
        rb.intake.setPower(speed);
    }

    void intakeOff() {
        rb.intake.setPower(0);
    }

    public void waitForDriveMotors() {
        while (rb.leftFrontDrive.isBusy() || rb.rightFrontDrive.isBusy() || rb.leftBackDrive.isBusy() || rb.rightBackDrive.isBusy()) {
            telemetry.update();
        }

        rb.leftFrontDrive.setPower(0);
        rb.rightFrontDrive.setPower(0);
        rb.leftBackDrive.setPower(0);
        rb.rightBackDrive.setPower(0);
    }

    public void waitForDriveMotorsFast() {
        while (rb.leftFrontDrive.isBusy() && rb.rightFrontDrive.isBusy() && rb.leftBackDrive.isBusy() && rb.rightBackDrive.isBusy()) {
            telemetry.update();
        }

        rb.leftFrontDrive.setPower(0);
        rb.rightFrontDrive.setPower(0);
        rb.leftBackDrive.setPower(0);
        rb.rightBackDrive.setPower(0);
    }

    public void waitForArm() {
        while (rb.robotArm.isBusy())
        {
            telemetry.update();
        }


    }

    public void spinnerBlue(double speed) {
        rb.spinner.setPower(-speed);
    }

    public void spinnerRed(double speed) {
        rb.spinner.setPower(speed);
    }

    public void spinnerEnd() {
        rb.spinner.setPower(0);
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = rb.VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        rb.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        rb.tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, rb.vuforia);
        rb.tfod.loadModelFromAsset(rb.TFOD_MODEL_ASSET, rb.LABELS);
    }

    public int getElement(){

        if (rb.tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = rb.tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                // telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (int p = 0; p != updatedRecognitions.size(); p++) {
                        /*telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());*/

                    if(updatedRecognitions.get(i).getLabel() != "Marker") {
                        telemetry.addData("", updatedRecognitions.get(i).getLeft());
                    }
                    if(updatedRecognitions.get(i).getLeft() < 180){
                        return 1;
                    }else{
                        return 2;
                    }


                }
                if(updatedRecognitions.size() == 0){
                    return 3;
                }


            }

        }
        return hubNum;
    }




}