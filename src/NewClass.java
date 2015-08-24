
import com.eztech.mechrinka.MasterMessage;
import com.eztech.mechrinka.clientside.Machine;
import com.eztech.mechrinka.serverside.MachineInterface;
import com.eztech.mechrinka.serverside.MachineServer;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yami
 */
//import com.Main.Data;
public class NewClass {

    public static void main(String[] args) {
        try {

            MachineServer ss = new MachineServer(new MachineInterface() {

                @Override
                public void initGcode() {
                    System.out.println("Init Gcode");
                }

                @Override
                public void addGcodeLine(String gcode) {
                    System.out.println("Line:" + gcode);
                }

                @Override
                public void endGcode() {
                    System.out.println("End Gcode");
                }

                @Override
                public String getName() {
                    return "Ez-Mill";
                }
            });
            List<Machine> list = Machine.getAvailableMachines();
            Thread.sleep(1000);
            System.out.println("Machines Found: " + list.size());
            list.stream().forEach(o -> System.out.println(o));
            list.get(0).sendMessage(MasterMessage.InitGcode);
            list.get(0).sendMessage(MasterMessage.AddGcodeLine, "HIIIIIIIIIII");
            list.get(0).sendMessage(MasterMessage.EndGcode);

            list.get(0).closeConnection();
//            while (true) {
//                list.addAll(Machine.getAvailableMachines());
//                Thread.sleep(2000);
//                int ind = (int) ((Math.random()*100)%list.size());
//                list.get(ind).sendMessage(MasterMessage.ConnectionTest,"-->Bye");
//                list.get(ind).closeConnection();
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
