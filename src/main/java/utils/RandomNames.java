package utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomNames {
        private static String[] First = { "Алексей", "Артём", "Вадим", "Владимир", "Валентин",
                "Данил", "Денис", "Дмитрий", "Егор", "Кирилл", "Леонид", "Максим", "Матвей", "Никита",
                "Олег", "Павел", "Пётр", "Роман", "Сергей", "Станислав", "Святослав", "Семён", "Тимур",
                "Фёдор", "Харитон", "Эдуард", "Юрий", "Яков", "Ярослав" };
        private static String[] Middle = { "Александрович", "Богданович", "Вадимович", "Гаврилович", "Давыдович", "Евгеньевич",
                "Кириллович", "Леонидович", "Назарович", "Робертович", "Тарасович", "Эдуардович", "Олегович", "Савельевич", "Федорович",
                "Юрьевич", "Павлович", "Яковлевич" };
        private static String[] Last = { "Иванов", "Смирнов", "Кузнецов", "Попов", "Васильев", "Алексеев", "Лебедев",
                "Семёнов", "Егоров", "Павлов", "Козлов", "Степанов", "Николаев", "Карпов", "Сафонов", "Зыков", "Щукин" };

        private static Random rand = new Random();

        public static String FirstName() {
            return First[rand.nextInt(First.length)];
        }
        public static String MiddleName() {
                return Middle[rand.nextInt(Middle.length)];
        }
        public static String LastName() {
                return Last[rand.nextInt(Last.length)];
        }


}
