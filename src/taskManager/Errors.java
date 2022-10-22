package taskmanager;

public class Errors {
    public static void errorMessage(int errorNumber) { // Обработчик ошибок
        switch (errorNumber) {
            case 0:
                System.out.println("Задача создана!");
                break;
            case 1:
                System.out.println("Задача не создана!\n"
                        + "Не указанно наименование задачи!");
                break;
            case 2:
                System.out.println("Задача не создана!\n"
                        + "Некорректно задана родительская задача!");
                break;
            case 3:
                System.out.println("Объект не распознан, повторите импорт!");
                break;
            case 4:
                System.out.println("Вызван не верный класс метода!");
                break;
            default:
                break;
        }
    }
}
