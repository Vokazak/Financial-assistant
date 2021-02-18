package ru.vokazak;

/*
Artyom Vasiliev, 12o34p, artvas@gmail.com
Vladimir Kondratiev, 4r32y1, vladkond@gmail.com
Anton Vlasov, s89f02, antvlas@gmail.com
Polina Grigorieva, 8h475L, polgrig@gmail.com
Nikita Andreev, 6t037i, nikandr@gmail.com
Inna Vdovina, 9t3b41, invdov@gmail.com

CMD_REGISTER("register"),
CMD_LOGIN("login"),
CMD_GET_ACCS("show_acc_list"),
CMD_CREATE_ACC("create_acc"),
CMD_DELETE_ACC("del_acc"),
CMD_DISCONNECT("disconnect"),
*/

public class Main {

    public static void main(String[] args) {

        boolean next = true;
        RequestHandler rh = new RequestHandler();

        while (next) {
            try {
                next = rh.processNewRequest();
                System.out.println();

            } catch (UnsuccessfulCommandExecutionExc e) {
                e.printStackTrace();
            }
        }
    }

}
