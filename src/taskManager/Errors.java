package taskmanager;

public class Errors {
    private Errors() {

    }

    public static void errorMessage(int errorNumber) { // Обработчик ошибок
        switch (errorNumber) {
            case 0:
                System.out.println("Задача не создана!\n"
                        + "Не указанно наименование задачи!");
                break;
            case 1:
                System.out.println("Задача не создана!\n"
                        + "Некорректно задана родительская задача!");
                break;
            default:
                System.out.println("Возникла проблема!");
                break;
        }
    }
}