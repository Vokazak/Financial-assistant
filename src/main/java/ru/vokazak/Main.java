package ru.vokazak;

import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.view.RequestHandler;

/*
Artyom Vasiliev, 12o34p, artvas@gmail.com
Vladimir Kondratiev, 4r32y1, vladkond@gmail.com
Anton Vlasov, s89f02, antvlas@gmail.com
Polina Grigorieva, 8h475L, polgrig@gmail.com
Nikita Andreev, 6t037i, nikandr@gmail.com
Inna Vdovina, 9t3b41, invdov@gmail.com

register
login
show_acc_list
create_acc
del_acc
disconnect
create_trans_type
del_trans_type
modify_trans_type
show_stats_for

login artvas@gmail.com 12o34p
*/

public class Main {

    public static void main(String[] args) {
        boolean next = true;
        RequestHandler rh = new RequestHandler();

        while (next) {
            try {
                next = rh.processNewRequest();
            } catch (UnsuccessfulCommandExecutionExc e) {
                e.printStackTrace();
            } finally {
                System.out.println();
            }
        }
    }

}
