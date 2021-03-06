package be.roommate.app.view.technical.navigation;

import android.support.v4.app.Fragment;
import be.roommate.app.R;
import be.roommate.app.view.fragment.Welcome.WelcomeFragment;
import be.roommate.app.view.fragment.about.AboutFragment;
import be.roommate.app.view.fragment.about.ContactFragment;
import be.roommate.app.view.fragment.about.FAQFragment;
import be.roommate.app.view.fragment.admin.PreferenceFragment;
import be.roommate.app.view.fragment.admin.RoommateFragment;
import be.roommate.app.view.fragment.chore.ChoreFragment;
import be.roommate.app.view.fragment.count.ResumeFragment;
import be.roommate.app.view.fragment.count.TicketListFragment;
import be.roommate.app.view.fragment.profile.MyProfileFragment;
import be.roommate.app.view.fragment.shopping.ShoppingItemListFragment;

/**
 * Created by florian on 5/02/15.
 */
public class MenuManager {

    public static enum MenuElement {
        MENU_EL_HOME(R.string.nav_drawer_welcome, 0, false, R.drawable.icon_home,SubMenuElement.HOME),
        MENU_EL_COUNT(R.string.nav_drawer_count, 1, false, R.drawable.icon_count,SubMenuElement.COUNT_RESUME, SubMenuElement.COUNT_TICKET_LIST),
        MENU_EL_SHOPPING(R.string.nav_drawer_shopping, 2, false, R.drawable.icon_shopping,SubMenuElement.SHOPPING_LIST),
        MENU_EL_CHORE(R.string.nav_drawer_chore, 3, false, R.drawable.icon_chore,SubMenuElement.CHORE_LIST),
        MENU_EL_PROFILE(R.string.nav_drawer_my_profile, 4, false, R.drawable.icon_profile,SubMenuElement.PROFILE_MY_PROFILE),
        MENU_EL_CONFIG(R.string.nav_drawer_config, 5, true, R.drawable.icon_config,SubMenuElement.ADMIN_ROOMMATE_LIST, SubMenuElement.ADMIN_PREFERENCE),
        MENU_EL_ABOUT(R.string.nav_drawer_about_about, 6, false, R.drawable.icon_about,SubMenuElement.ABOUT_ABOUT, SubMenuElement.ABOUT_FAQ, SubMenuElement.ABOUT_CONTACT);

        private final int name;
        private final int order;
        private boolean onlyForAdmin;
        private final SubMenuElement[] subMenuElements;
        private int icon;

        private MenuElement(int name, int order, boolean onlyForAdmin, int icon,SubMenuElement... subMenuElements) {
            this.name = name;
            this.order = order;
            this.onlyForAdmin = onlyForAdmin;
            this.icon = icon;
            //this.pagerClass = pagerClass;
            this.subMenuElements = subMenuElements;
        }

        public boolean isOnlyForAdmin() {
            return onlyForAdmin;
        }

        public int getName() {
            return name;
        }

        public int getOrder() {
            return order;
        }

        public SubMenuElement[] getSubMenuElements() {
            return subMenuElements;
        }

        public static SubMenuElement getSubMenuElementByPosition(int menuElementOrder, int subMenuElementOrder) {
            for (MenuElement menuElement : MenuElement.values()) {
                if (menuElement.getOrder() == menuElementOrder) {
                    return menuElement.getSubMenuElements()[subMenuElementOrder];
                }
            }
            return null;
        }

        public static MenuElement getByOrder(int position) {
            for (MenuElement menuElement : MenuElement.values()) {
                if (menuElement.getOrder() == position) {
                    return menuElement;
                }
            }

            return null;
        }

        public static MenuElement getByClass(Class<? extends Fragment> aClass) {
            for (MenuElement menuElement : values()) {
                for (SubMenuElement subMenuElement : menuElement.getSubMenuElements()) {
                    if (subMenuElement.fragmentClass.equals(aClass)) {
                        return menuElement;
                    }
                }
            }
            return null;
        }

        public int getIcon() {
            return icon;
        }
    }


    public static enum SubMenuElement {
        ADMIN_ROOMMATE_LIST(R.string.nav_config_roommate, RoommateFragment.class),
        ADMIN_PREFERENCE(R.string.nav_config_preference, PreferenceFragment.class),

        COUNT_RESUME(R.string.nav_count_resume, ResumeFragment.class),
        COUNT_TICKET_LIST(R.string.nav_count_ticket, TicketListFragment.class),

        PROFILE_MY_PROFILE(R.string.nav_drawer_my_profile, MyProfileFragment.class),

        CHORE_LIST(R.string.nav_drawer_chore, ChoreFragment.class),

        SHOPPING_LIST(R.string.nav_shopping_item, ShoppingItemListFragment.class),

        HOME(R.string.g_welcome, WelcomeFragment.class),

        ABOUT_ABOUT(R.string.nav_drawer_about_about, AboutFragment.class),
        ABOUT_FAQ(R.string.nav_drawer_about_faq, FAQFragment.class),
        ABOUT_CONTACT(R.string.nav_drawer_about_contact, ContactFragment.class);

        public Fragment getFragment() {
            switch (this) {

                case ADMIN_ROOMMATE_LIST:
                    return new RoommateFragment();
                case ADMIN_PREFERENCE:
                    return new PreferenceFragment();
                case COUNT_RESUME:
                    return new ResumeFragment();
                case COUNT_TICKET_LIST:
                    return new TicketListFragment();
                case PROFILE_MY_PROFILE:
                    return new MyProfileFragment();
                case CHORE_LIST:
                    return new ChoreFragment();
                case SHOPPING_LIST:
                    return new ShoppingItemListFragment();
                case HOME:
                    return new WelcomeFragment();
                case ABOUT_ABOUT:
                    return new AboutFragment();
                case ABOUT_CONTACT:
                    return new ContactFragment();
                case ABOUT_FAQ:
                    return new FAQFragment();

            }
            return null;
        }


        //private final int order;
        private final int name;
        private final Class<? extends Fragment> fragmentClass;

        SubMenuElement(int name, Class<? extends Fragment> fragmentClass) {
            this.name = name;
            this.fragmentClass = fragmentClass;
        }

        public Integer getOrder() {
            SubMenuElement[] subMenuElements = MenuElement.getByClass(this.fragmentClass).getSubMenuElements();
            for (int i = 0; i < subMenuElements.length; i++) {
                if (subMenuElements[i].equals(this.fragmentClass)) {
                    return i;
                }
            }
            return null;
        }

        public int getName() {
            return name;
        }

        public static SubMenuElement getByClass(Class<?> pagerClass) {
            for (SubMenuElement subMenuElement : values()) {
                if (subMenuElement.fragmentClass.equals(pagerClass)) {
                    return subMenuElement;
                }
            }
            return null;
        }

    }


}
