/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eztech.mechrinka.serverside;

/**
 *
 * @author yami
 */
public interface MachineInterface {

    public void initGcode();

    public void addGcodeLine(String gcode);

    public void endGcode();

    public String getName();

}
