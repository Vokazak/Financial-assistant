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

Authorization commands:
register (email, password, name, surname)
login (email, password)

Account commands:
show_acc_list ()
create_acc (name, balance)
del_acc (name)

Category commands:
create_trans_type (name)
del_trans_type (name)
modify_trans_type (oldName, newName)

Transaction commands:
create_trans (name, accFrom, accTo, category, money)

Reports:
show_stats_for (days)

disconnect ()

Example:
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
