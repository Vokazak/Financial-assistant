package ru.vokazak;

import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.view.RequestHandler;

/*
12o34p, artvas@gmail.com
4r32y1, vladkond@gmail.com
s89f02, antvlas@gmail.com
8h475L, polgrig@gmail.com
6t037i, nikandr@gmail.com
9t3b41, invdov@gmail.com

register
login
show_acc_list
create_acc
del_acc
disconnect
create_trans_type
del_trans_type
modify_trans_type
show_stats_for (days)
create_trans (name, accFrom, accTo, category, money)

login artvas@gmail.com 12o34p
create_trans TestTransaction AccForSalary AccForEverydayTrans TransferBetweenOwnAccs 3000
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
